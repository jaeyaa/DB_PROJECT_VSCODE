package site.metacoding.hospital;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseDto {
    public Response response;

    @AllArgsConstructor
    @Data
    public class Response {

        public Header header;
        public Body body;

        @AllArgsConstructor
        @Data
        public class Header {

            public String resultCode;
            public String resultMsg;

        } // end of Header

        @AllArgsConstructor
        @Data
        public class Body {

            public Items items;
            public int numOfRows;
            public int pageNo;
            public int totalCount;

            @AllArgsConstructor
            @Data
            public class Items {

                public List<Item> item;

                @AllArgsConstructor
                @Data
                public class Item {

                    public String addr; // 주소
                    public int mgtStaDd; // 운영시작일자
                    public String pcrPsblYn; // 구분코드
                    public String ratPsblYn; // RAT 가능여부
                    public int recuClCd; // 요양종별코드
                    public String rprtWorpClicFndtTgtYn; // 호흡기전담클리닉 여부
                    public String sgguCdNm; // 시군구명
                    public String sidoCdNm; // 시도명
                    public String telno; // 전화번호
                    public int XPos; // x 좌표
                    public String XPosWgs84; // 세계지구 x 좌표
                    public int YPos; // y 좌표
                    public String YPosWgs84; // 세계지구 y 좌표
                    public String yadmNm; // 요양기관명
                    public String ykihoEnc; // 암호화된 요양기호

                } // end of Item
            } // end of Items
        } // end of Body
    } // end of Response
}