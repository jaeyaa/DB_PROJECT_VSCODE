package site.metacoding.hospital;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import site.metacoding.hospital.ResponseDto.Response.Body.Items.Item;

public class MainApp {

    int totalCount;

    public static void main(String[] args) {

        // 1. URL
        try {
            StringBuffer addr = new StringBuffer();
            addr.append("http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService?");
            addr.append(
                    "serviceKey=wJmmW29e3AEUjwLioQR22CpmqS645ep4S8TSlqtSbEsxvnkZFoNe7YG1weEWQHYZ229eNLidnI2Yt5EZ3Stv7g%3D%3D&");
            addr.append("pageNo=1&");
            addr.append("numOfRows=10&");
            addr.append("_type=json");

            URL url = new URL(addr.toString());

            // 2. ByteStream 연결
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 3. Buffer
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"));

            // 4. flush
            String responsJson = br.readLine();
            // System.out.println(responsJson);

            // 5. GSON -> 자바 오브젝트로 변경하기
            Gson gson = new Gson();
            ResponseDto dto = gson.fromJson(responsJson, ResponseDto.class);
            // System.out.println(dto);

            // 다시 버퍼하기 (pcr 검사 가능 병원 현황 데이터 총 개수만큼 DB에 INSERT)
            // 1. URL
            int totalCount = dto.getResponse().getBody().getTotalCount();
            addr = new StringBuffer();
            addr.append("http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService?");
            addr.append(
                    "serviceKey=wJmmW29e3AEUjwLioQR22CpmqS645ep4S8TSlqtSbEsxvnkZFoNe7YG1weEWQHYZ229eNLidnI2Yt5EZ3Stv7g%3D%3D&");
            addr.append("pageNo=1&");
            addr.append("numOfRows=" + totalCount + "&");
            addr.append("_type=json");

            url = new URL(addr.toString());

            // 2. ByteStream 연결
            conn = (HttpURLConnection) url.openConnection();

            // 3. Buffer
            br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"));

            // 4. flush
            responsJson = br.readLine();

            // 5. GSON -> 자바 오브젝트로 변경하기
            gson = new Gson();
            dto = gson.fromJson(responsJson, ResponseDto.class);

            // 6. Dto의 item 정보들을 자바 오브젝트(Hospital)에 옮겨담기
            List<Item> rs = dto.getResponse().getBody().getItems().getItem();
            List<Hospital> hospitals = new ArrayList<>();
            for (int i = 0; i < rs.size(); i++) {
                Hospital hospital = new Hospital(rs.get(i).getAddr(), rs.get(i).mgtStaDd, rs.get(i).pcrPsblYn,
                        rs.get(i).ratPsblYn, rs.get(i).recuClCd, rs.get(i).rprtWorpClicFndtTgtYn, rs.get(i).sgguCdNm,
                        rs.get(i).sidoCdNm, rs.get(i).telno, rs.get(i).XPos, rs.get(i).XPosWgs84, rs.get(i).YPos,
                        rs.get(i).YPosWgs84, rs.get(i).yadmNm, rs.get(i).ykihoEnc);
                hospitals.add(hospital);
            }

            // 7. 자바 오브젝트 -> DB로 INSERT 하기
            // 7-1) connection 연결 (세션생성) port, ip, id, password, protocol
            Connection conn1 = DriverManager.getConnection // conn = socket
            ("jdbc:oracle:thin:@localhost:1521:xe", "SCOTT", "TIGER");
            System.out.println("DB연결완료");

            // 7-2) 버퍼 달아서 통신 (ALL:SELECT * FROM emp)
            String sql = "INSERT INTO hospital(addr, mgtStaDd, pcrPsblYn, ratPsblYn, recuClCd, rprtWorpClicFndtTgtYn, sgguCdNm, sidoCdNm, telno, XPos, XPosWgs84,YPos, YPosWgs84, yadmNm, ykihoEnc) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pstmt = conn1.prepareStatement(sql); // 프로토콜이 적용된 버퍼
            for (int i = 0; i < rs.size(); i++) {
                pstmt.setString(1, hospitals.get(i).getAddr()); // 물음표의 순서, 값
                pstmt.setInt(2, hospitals.get(i).getMgtStaDd());
                pstmt.setString(3, hospitals.get(i).getPcrPsblYn());
                pstmt.setString(4, hospitals.get(i).getRatPsblYn());
                pstmt.setInt(5, hospitals.get(i).getRecuClCd());
                pstmt.setString(6, hospitals.get(i).getRprtWorpClicFndtTgtYn());
                pstmt.setString(7, hospitals.get(i).getSgguCdNm());
                pstmt.setString(8, hospitals.get(i).getSidoCdNm());
                pstmt.setString(9, hospitals.get(i).getTelno());
                pstmt.setInt(10, hospitals.get(i).getXPos());
                pstmt.setString(11, hospitals.get(i).getXPosWgs84());
                pstmt.setInt(12, hospitals.get(i).getYPos());
                pstmt.setString(13, hospitals.get(i).getYPosWgs84());
                pstmt.setString(14, hospitals.get(i).getYadmNm());
                pstmt.setString(15, hospitals.get(i).getYkihoEnc());

                // 에러 : -1, 성공 : 수행된(생성, 삭제, 수정) row 개수, 아무 변화가 없으면 0
                int result = pstmt.executeUpdate(); // DELETE, UPDATE, INSERT (내부에 commit 존재)

                if (result > 0) {
                    System.out.println("성공"); // 1
                } else {
                    System.out.println("실패"); // 0
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // 오류 추적
        }
    }
}