<template>
  <div>
    <div id="map"></div>
  </div>
</template>

<script>
export default {
  name: "KakaoMap",
  props: {
    favorites: Array,
  },
  mounted() {
    if (window.kakao && window.kakao.maps) {
      this.initMap();
    } else {
      const script = document.createElement("script");
      /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap);
      script.src =
        "//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=915cffed372954b7b44804ed422b9cf0";
      document.head.appendChild(script);
    }

    var mapContainer = document.getElementById("map"), // 지도를 표시할 div
      mapCenter = new kakao.maps.LatLng(37.546475, 126.9646916), // 지도의 중심 좌표
      mapOption = {
        center: mapCenter, // 지도의 중심 좌표
        level: 8, // 지도의 확대 레벨
      };

    // 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
    var map = new kakao.maps.Map(mapContainer, mapOption);

    // 지도에 올릴 마커를 생성합니다.
    for (var i = 0; i < this.favorites.length; i++) {
      var loc = new kakao.maps.LatLng(
        this.favorites[i].lat,
        this.favorites[i].lng,
      );
      var mMarker = new kakao.maps.Marker({
        position: loc, // 지도의 중심좌표에 올립니다.
        map: map, // 생성하면서 지도에 올립니다.
      });

      // 지도에 올릴 장소명 인포윈도우 입니다.
      var mLabel = new kakao.maps.InfoWindow({
        position: loc, // 지도의 중심좌표에 올립니다.
        content: this.favorites[i].aptName, // 인포윈도우 내부에 들어갈 컨텐츠 입니다.
      });

      mLabel.open(map, mMarker); // 지도에 올리면서, 두번째 인자로 들어간 마커 위에 올라가도록 설정합니다.
    }
  },
  methods: {
    initMap() {
      const container = document.getElementById("map");
      const options = {
        center: new kakao.maps.LatLng(33.450701, 126.570667),
        level: 5,
      };

      //지도 객체를 등록합니다.
      //지도 객체는 반응형 관리 대상이 아니므로 initMap에서 선언합니다.
      this.map = new kakao.maps.Map(container, options);
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#map {
  width: 500px;
  height: 500px;
}

.button-group {
  margin: 10px 0px;
}

button {
  margin: 0 3px;
}
</style>
