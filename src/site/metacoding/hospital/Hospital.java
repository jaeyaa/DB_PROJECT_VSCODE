package site.metacoding.hospital;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Hospital {
    private String addr; // 주소
    private int mgtStaDd; // 운영시작일자
    private String pcrPsblYn; // 구분코드
    private String ratPsblYn; // RAT가능여부
    private int recuClCd; // 요양종별코드
    private String rprtWorpClicFndtTgtYn; // 호흡기전담클리닉 여부
    private String sgguCdNm; // 시군구명
    private String sidoCdNm; // 시도명
    private String telno; // 전화번호
    private int XPos; // x좌표
    private String XPosWgs84; // 세계지구x좌표
    private int YPos; // y좌표
    private String YPosWgs84; // 세계지구y좌표
    private String yadmNm; // 요양기관명
    private String ykihoEnc; // 암호화된요양기호

}