<template>
  <b-container class="bv-example-row mt-3">
    <b-row>
      <b-col>
        <b-alert show><h3>글보기</h3></b-alert>
      </b-col>
    </b-row>
    <b-row class="mb-1">
      <b-col class="text-left">
        <b-button variant="outline-primary" @click="listArticle">목록</b-button>
      </b-col>
      <b-col class="text-right">
        <b-button
          variant="outline-info"
          size="sm"
          @click="moveModifyArticle"
          class="mr-2"
          v-show="islogin"
          >글 수정</b-button
        >
        <b-button
          variant="outline-danger"
          size="sm"
          @click="deleteArticle"
          v-show="islogin"
          >글 삭제</b-button
        >
      </b-col>
    </b-row>
    <b-row class="mb-1">
      <b-col>
        <b-card
          :header-html="`<h3>${article.qno}.
          [${article.category}] ${article.subject}</h3><div><h6>작성자 : ${article.writer}</div><div>등록 시간 : ${article.regDate}</h6></div>`"
          class="mb-2"
          border-variant="dark"
          no-body
        >
          <b-card-body class="text-left">
            <div v-html="message"></div>
          </b-card-body>
        </b-card>
      </b-col>
    </b-row>
    <answer-list-item
      v-bind:qno="`${article.qno}`"
      v-bind:subject="article.subject"
      v-bind:isManager="isManager"
      v-for="answer in answers"
      :key="answer.ano"
      v-bind="answer"
    ></answer-list-item>
    <answer-input-item
      v-show="isManager"
      v-bind:qno="`${article.qno}`"
    ></answer-input-item>
  </b-container>
</template>

<script>
import http from "@/api/http";
import AnswerInputItem from "@/components/qna/item/AnswerInputItem.vue";
import AnswerListItem from "@/components/qna/item/AnswerListItem.vue";
import { mapState, mapMutations } from "vuex";

const memberStore = "memberStore";

export default {
  components: { AnswerInputItem, AnswerListItem },
  name: "QuestionDetail",
  data() {
    return {
      article: {},
      islogin: false,
      isManager: false,
      answers: [],
    };
  },
  computed: {
    ...mapState(memberStore, ["userInfo"]),
    message() {
      if (this.article.content)
        return this.article.content.split("\n").join("<br>");
      return "";
    },
  },
  created() {
    let userInfoCheck = this.userInfo != null;
    let useridValue = userInfoCheck ? this.userInfo.memberId : "";
    http
      .get(`/question/${this.$route.params.qno}?userid=${useridValue}`)
      .then(({ data }) => {
        if (data === "fail") {
          alert("접근 권한이 없습니다.");
          this.$router.push({ name: "questionList" });
        } else {
          this.article = data.question;
        }
        if (
          (userInfoCheck && this.article.writer == this.userInfo.memberId) ||
          this.userInfo.isManager == "Y"
        )
          this.islogin = true;
        if (this.userInfo != null && this.userInfo.isManager === "Y")
          this.isManager = true;
      });
    http.get(`/answer/${this.$route.params.qno}`).then(({ data }) => {
      this.answers = data;
    });
  },
  methods: {
    ...mapMutations(memberStore, ["SET_USER_INFO"]),
    listArticle() {
      this.$router.push({ name: "questionList" });
    },
    moveModifyArticle() {
      this.$router.replace({
        name: "questionUpdate",
        params: { qno: this.$route.params.qno },
      });
      //   this.$router.push({ path: `/board/modify/${this.article.articleno}` });
    },
    deleteArticle() {
      if (confirm("삭제하시겠습니까?")) {
        http.delete(`/question/delete/${this.$route.params.qno}`);
        alert("삭제되었습니다.");
        this.$router.push({ name: "questionList" });
      }
    },
  },
};
</script>

<style></style>
