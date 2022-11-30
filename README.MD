# Nghiệp vụ

## Nghiệp vụ 1: 
Hệ thống lấy danh sách trận đấu với thông tin chi tiết (ngày, giờ, đội bóng, kết quả) từ các nguồn

## Nghiệp vụ 2:
Hệ thống post thông tin các trận đấu đã rõ các thông tin (ngày, giờ, đội bóng) lên Slack

## Nghiệp vụ 3:
Hệ thống ghi nhận bình chọn của người chơi qua reply ở Slack. Bình chọn không hợp lệ là:

- thời điểm T là 5 phút trước khi trận đấu diễn ra, bình chọn sau thời điểm T là không hợp lệ
- bình chọn sai tên đội hoặc không phải là "Hoà", "Hòa", "hoà", "hòa" là không hợp lệ

#### Nghiệp vụ 3.1:
Khi ghi nhận bình chọn của người chơi, nếu người chơi không có ở tournament_board thì thêm vào

#### Nghiệp vụ 3.2
Khi bình chọn không hợp lệ hệ thống thông báo cho người chơi biết

## Nghiệp vụ 4:
Hệ thống chấm điểm người chơi sau khi trận đấu kết thúc và lấy được kết quả

#### Nghiệp vụ 4.1:
Sau khi chấm điểm người chơi, hệ thống cập nhật tổng điểm và vị trí của người  chơi ở tournament_board

#### Nghiệp vụ 4.2:
Sau khi tổng hợp điểm, hệ thống gửi bảng xếp hạng mới nhất vào Slack

## Nghiệp vụ 5:
Hệ thống thông báo cho người chơi bình chọn trước khi trận đấu diễn ra 12h nếu người chơi chưa bình chọn


# Thiết kế CSDL
https://dbdiagram.io/d/638178f1c9abfc61117548d0


# Các end point
- /slack/interactive-endpoint cổng 8080 nhận tương tác từ người dùng 
- /slack/command/leader-board cổng 8080 xem leader board
- /slack/command/my-bet cổng 8080 xem lịch sử bet của mình
- /slack cổng 3000 nhận event

# Tham khảo
- Lịch thi đấu và kết quả ở Google https://www.google.com/search?client=firefox-b-d&q=world+cup+2022#sie=lg;/m/0fp_8fm;2;/m/030q7;mt;fp;1;;;
- Lịch thi đấu và kết quả ở 24h https://www.24h.com.vn/vck-world-cup-2022/ket-qua-thi-dau-bong-da-world-cup-2022-c860a1369011.html
- Tool hỗ trợ thiết kế layout message https://app.slack.com/block-kit-builder
- Xử lý khi người dùng tương tác với message https://api.slack.com/interactivity/handling