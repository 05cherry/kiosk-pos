show databases;
show tables;
cretae database cafe2022;
SELECT * FROM cash;
SELECT * FROM product;
SELECT * FROM customer;
SELECT * FROM order_main;
SELECT * FROM order_each;
SELECT * FROM receipt_Number;

//create_ table_
CREATE TABLE material(
	code int not null primary key,
	name varchar(255)  not null,
	price int  not null,
	outOfDate Date not null, 
	num int not null
) default character set = euckr;

CREATE TABLE category(
	code int not null primary key,
	name varchar(255)  not null
) default character set = euckr;

CREATE TABLE product(
	code int not null primary key,
	name varchar(255)  not null,
	category int not null,
	price int  not null,
	imgSrc varchar(255),
	soldOut boolean not null,
	FOREIGN KEY (category) REFERENCES category(code) ON UPDATE CASCADE ON DELETE RESTRICT
) default character set = euckr;

CREATE TABLE customer (
	code int not null primary key,
	phone varchar(255) not null,
	point int not null
) default character set = euckr;

/*customerCode 연결된 회원이 없을 때 -1값으로 설정*/
CREATE TABLE order_main (
	code int not null primary key,
	customerCode int not null, 
	dateTime timestamp not null,
	payAmount int  not null,
	usePoint int not null,
	payType varchar(255) not null,
	takeOut boolean not null,
	FOREIGN KEY (customerCode) REFERENCES customer(code) ON UPDATE CASCADE ON DELETE RESTRICT
) default character set = euckr;

CREATE TABLE receipt_Number (
	order_main_code int not null primary key,
	receiptNumber int not null,
	FOREIGN KEY (order_main_code) REFERENCES order_main(code) ON UPDATE CASCADE ON DELETE CASCADE
) default character set = euckr;

/*size true => "Large"*/
CREATE TABLE order_each (
	order_main_code int not null,
	product_code int not null,
	eachPrice int not null,
	eachNum int not null,
	size boolean not null,
	shot boolean not null,
	hotOrIce boolean not null,
	cream boolean not null,
	hazelSyrup boolean not null,
	almondSyrup boolean not null,
	vanillaSyrup boolean not null,
	PRIMARY KEY (order_main_code, product_code),
	FOREIGN KEY (order_main_code) REFERENCES order_main(code) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (product_code) REFERENCES product(code) ON UPDATE CASCADE ON DELETE CASCADE
) default character set = euckr;


CREATE TABLE cash (
	code int not null PRIMARY KEY,
	currentCash int not null
) default character set = euckr;

/*insert_ data_*/
INSERT INTO material values (1000, '두유', 5500, "2022-12-25", 30);
INSERT INTO material values (1001, '일반 우유', 5500, "2022-12-25", 25);
INSERT INTO material values (1002, '딸기', 10000, "2022-12-25", 10);
INSERT INTO material values (1003, '카라멜 시럽', 12000, "2022-12-25", 20);
INSERT INTO material values (1004, '녹차 파우더', 12000, "2022-12-25", 30);
INSERT INTO material values (1005, '헤이즐넛 시럽', 11000, "2023-12-21", 3);
INSERT INTO material values (1006, '아몬드 시럽', 11000, "2023-12-18", 5);
INSERT INTO material values (1007, '카라멜 시럽', 11000, "2023-12-11", 4);
INSERT INTO material values (1008, '모카 파우더', 8000, "2022-12-31", 1);
INSERT INTO material values (1009, '바닐라 파우더', 8000, "2023-01-25", 5);
INSERT INTO material values (1010, '바닐라 시럽', 11000, "2023-07-05", 4);
INSERT INTO material values (1011, '콜드브루 원액', 9000, "2022-12-25", 1);
INSERT INTO material values (1012, '삼다수', 3000, "2023-01-31", 8);
INSERT INTO material values (1013, '원두', 12000, "2022-12-25", 8);
INSERT INTO material values (1014, '연유', 7000, "2023-02-11", 4);
INSERT INTO material values (1015, '바닐라 아이스크림', 11000, "2023-04-01", 10);
INSERT INTO material values (1016, '잉글리쉬 블랙버스트 티', 10000, "2024-12-31", 7);
INSERT INTO material values (1017, '얼그레이 티', 10000, "2024-12-25", 6);
INSERT INTO material values (1018, '민트 티', 9000, "2024-12-25", 7);
INSERT INTO material values (1019, '카모마일 티', 9000, "2023-12-25", 3);
INSERT INTO material values (1020, '크림 카라멜 티', 12000, "2024-12-11", 4);
INSERT INTO material values (1021, '애플 민트 티', 11000, "2022-12-25", 1);
INSERT INTO material values (1022, '허니 레몬 티', 12000, "2024-12-20", 7);
INSERT INTO material values (1023, '밀크티 원액', 12000, "2022-12-31", 1);
INSERT INTO material values (1024, '타피오카 펄', 7000, "2022-12-25", 4);
INSERT INTO material values (1025, '유자청', 7000, "2023-03-20", 2);
INSERT INTO material values (1026, '레몬 베이스', 9000, "2023-03-20", 2);
INSERT INTO material values (1027, '오렌지', 2000, "2022-12-25", 2);
INSERT INTO material values (1028, '오렌지 자몽 티', 12000, "2024-12-25", 5);
INSERT INTO material values (1029, '홀더', 5000, "2027-12-31", 10);
INSERT INTO material values (1030, '종이컵 레귤러', 6000, "2027-12-31", 5);
INSERT INTO material values (1031, '종이컵 라지', 7000, "2027-12-31", 5);
INSERT INTO material values (1032, '플라스틱컵 레귤러', 7000, "2027-12-31", 5);
INSERT INTO material values (1033, '플라스틱컵 라지', 8000, "2027-12-31", 5);
INSERT INTO material values (1034, '종이컵 레귤러 뚜껑', 3000, "2027-12-31", 3);
INSERT INTO material values (1035, '종이컵 라지 뚜껑', 3500, "2027-12-31", 3);
INSERT INTO material values (1036, '플라스틱컵 레귤러 뚜껑', 3500, "2027-12-31", 3);
INSERT INTO material values (1037, '플라스틱컵 라지 뚜껑', 4000, "2027-12-31", 3);

INSERT INTO category values (2000, 'Coffee');
INSERT INTO category values (2001, 'ColdBrew');
INSERT INTO category values (2002, 'Tea');
INSERT INTO category values (2003, 'TeaVariation');
INSERT INTO category values (2004, 'FruitTea');

INSERT INTO product values (3000, '아메리카노', 2000, 4500, 'images\\americano.jpg', true);
INSERT INTO product values (3001, '카페라떼', 2000, 5000, 'images\\cafelatte.jpg', true);
INSERT INTO product values (3002, '카푸치노', 2000, 5500, 'images\\cappuccino.png', true);
INSERT INTO product values (3003, '바닐라 카페라떼', 2000, 5900, 'images\\vanillaCafelatte.png', true);
INSERT INTO product values (3004, '카페모카', 2000, 3700, 'images\\cafemocha.jpg', true);
INSERT INTO product values (3005, '카라멜 마키아또', 2000, 5800, 'images\\caramelMacchiato.jpg', true);
INSERT INTO product values (3006, '에스프레소', 2000, 6100, 'images\\espresso.png', true);
INSERT INTO product values (3007, '스페니쉬 연유 카페라떼', 2000, 5900, 'images\\milkCafelatte.png', true);
INSERT INTO product values (3008, '아이스크림 카페라떼', 2000, 3700, 'images\\icecreamCafelatte.png', true);
INSERT INTO product values (3009, '콜드브루', 2001, 4900, 'images\\coldbrew.png', true);
INSERT INTO product values (3010, '콜드브루 라떼', 2001, 5400, 'images\\coldbrewLatte.png', true);
INSERT INTO product values (3011, '잉글리쉬 블랙퍼스트', 2002, 5100, 'images\\englishBreakfast.jpg', true);
INSERT INTO product values (3012, '얼그레이', 2002, 5100, 'images\\earlGrey.png', true);
INSERT INTO product values (3013, '민트', 2002, 5100, 'images\\mint.jpg', true);
INSERT INTO product values (3014, '카모마일', 2002, 5100, 'images\\chamomile.png', true);
INSERT INTO product values (3015, '크림 카라멜', 2002, 5100, 'images\\creamCaramel.png', true);
INSERT INTO product values (3016, '애플 민트티', 2003, 6100, 'images\\appleMint.jpg', true);
INSERT INTO product values (3017, '허니 레몬티', 2003, 6100, 'images\\honeyLemon.jpg', true);
INSERT INTO product values (3018, '버블 밀크티', 2003, 6000, 'images\\bubbleMilk.png', true);
INSERT INTO product values (3019, '버블 그린티 라떼', 2003, 5900, 'images\\bubbleGreenLatte.jpg', true);
INSERT INTO product values (3020, '로얄 밀크티', 2003, 5500, 'images\\royalMilk.jpg', true);
INSERT INTO product values (3021, '그린티 라떼', 2003, 5200, 'images\\greenLatte.jpg', true);
INSERT INTO product values (3022, '유자 레몬티', 2004, 5500, 'images\\citronLemon.png', true);
INSERT INTO product values (3023, '오렌지 자몽티', 2004, 5500, 'images\\orangeGrapefruit.png', true);

INSERT INTO customer values (-1, '000-0000-0000', 0);/*연결된 사용자가 없을 때 여기로 연결*/
INSERT INTO customer values (4000, '010-5632-4358', 5000);
INSERT INTO customer values (4001, '010-1234-5678', 4500);
INSERT INTO customer values (4002, '010-4564-8244', 1200);
INSERT INTO customer values (4003, '010-0219-0214', 300);
INSERT INTO customer values (4004, '010-0818-0820', 700);
INSERT INTO customer values (4005, '010-5657-8348', 5000);
INSERT INTO customer values (4006, '010-1111-1111', 5000);
INSERT INTO customer values (4007, '010-2222-2222', 700);
INSERT INTO customer values (4008, '010-3333-3333', 7000);
INSERT INTO customer values (4009, '010-4444-4444', 5000);
INSERT INTO customer values (4010, '010-5555-5555', 7000);
INSERT INTO customer values (4011, '010-6666-6666', 700);
INSERT INTO customer values (4012, '010-7777-7777', 7000);
INSERT INTO customer values (4013, '010-8888-8888', 7000);
INSERT INTO customer values (4014, '010-9999-9999', 11000);
INSERT INTO customer values (4015, '010-7756-2432', 10000);
INSERT INTO customer values (4016, '010-4555-6666', 4500);
INSERT INTO customer values (4017, '010-9876-5498', 4500);
INSERT INTO customer values (4018, '010-1616-1656', 4800);
INSERT INTO customer values (4019, '010-3232-3232', 10000);
INSERT INTO customer values (4020, '010-1981-9819', 300);
INSERT INTO customer values (4021, '010-7417-4146', 8900);
INSERT INTO customer values (4022, '010-0111-0114', 4800);
INSERT INTO customer values (4023, '010-1225-1231', 900);
INSERT INTO customer values (4024, '010-0618-0618', 4500);
INSERT INTO customer values (4025, '010-1221-9885', 10000);
INSERT INTO customer values (4026, '010-3222-2228', 4800);
INSERT INTO customer values (4027, '010-0214-0204', 4500);
INSERT INTO customer values (4028, '010-2017-0412', 4500);
INSERT INTO customer values (4029, '010-2022-1231', 10000);
INSERT INTO customer values (4030, '010-5556-6644', 17000);
INSERT INTO customer values (4031, '010-2021-1252', 15000);
INSERT INTO customer values (4032, '010-1998-0820', 500);
INSERT INTO customer values (4033, '010-1998-0219', 100);
INSERT INTO customer values (4034, '010-1997-0214', 600);
INSERT INTO customer values (4035, '010-1995-1230', 4000);

INSERT INTO order_main values (100001, 4000, "2027-12-31 12:10:12", 9500, 200, "카드", false);
INSERT INTO order_main values (100002, 4000, "2027-12-31 12:11:12", 9500, 500, "현금", false);
INSERT INTO order_main values (100003, 4035, "2027-12-31 12:12:20", 9500, 500, "현금", false);
INSERT INTO order_main values (100004, 4035, "2027-12-31 12:03:06", 9500, 500, "현금", false);

INSERT INTO receipt_Number values (100001, 001);
INSERT INTO receipt_Number values (100002, 002);
INSERT INTO receipt_Number values (100003, 003);
INSERT INTO receipt_Number values (100004, 004);

/** [주문번호: 100001] 아이스 아메리카노와 따듯한 라떼를 시킴 **/
INSERT INTO order_each values (100001, 3000, 4500, 1, false, false, true, false, false, false, false);
INSERT INTO order_each values (100001, 3001, 5000, 1, false, false, false, false, false, false, false);
/** [주문번호: 100002] 콜드브루라떼 2잔과 따듯한 민트를 시킴 **/
INSERT INTO order_each values (100002, 3010, 5400, 2, false, false, true, false, false, false, false);
INSERT INTO order_each values (100002, 3013, 5100, 1, false, false, false, false, false, false, false);
/** [주문번호: 100002] 콜드브루라떼 2잔과 따듯한 민트를 시킴 **/
INSERT INTO order_each values (100003, 3010, 5400, 2, false, false, true, false, false, false, false);
INSERT INTO order_each values (100003, 3013, 5100, 1, false, false, false, false, false, false, false);
/** [주문번호: 100002] 콜드브루라떼 2잔과 따듯한 민트를 시킴 **/
INSERT INTO order_each values (100004, 3010, 5400, 2, false, false, true, false, false, false, false);
INSERT INTO order_each values (100004, 3013, 5100, 1, false, false, false, false, false, false, false);

INSERT INTO cash values (1234, 300000);

//--------------------------------------------------------------material
SELECT * FROM material WHERE code = 1000 ;
SELECT * FROM material;
SELECT * FROM order_each;
SELECT * FROM order_main;
SELECT * FROM cash;
SHOW FULL COLUMNS FROM material;
DROP TABLE material;
//--------------------------------------------------------------category

//--------------------------------------------------------------product

//--------------------------------------------------------------customer

//--------------------------------------------------------------order_main

/* !!!결제 방식 [현금/카드]만 있는거 맞나요. 확인하고 알려주세요. !!!
 * dateTime를 TIMESTAMP로 설정해서, insert할 때 시각을 주문시간으로 설정했습니다.
 * 키오스크에서 쿼리 작성할 때 insert문  안에 now()해주시면 됩니다.
 * */
//--------------------------------------------------------------receipt_Number

//--------------------------------------------------------------order_each

/* order_main_code, product_code는 외래키이자 기본키
 * !!!eachPrice는 product의 가격인데 이거 insert할 때 바로 product.price에서 참조해서 집어넣을 수 없음.!!! < 꼭 필요한건지 의문입니다.
 * !!! 그리고 샷추가나 시럽추가로 가격이 인상될텐데 이거 eachPrice에 반영이 되는건가요..? 
 * 그러면 product.price 참조 안해도 되는건지 이부분 키오스크 정해서 꼭 알려주세요.!!!
 * [size] false : regular / true : large
 * [shot] false : 1 shot  / true : 2shot
 * [hotOrIce] false : hot / true : ice
 * [cream] false : X / true : O
 * [hazelSyrup] false : X / true : O
 * [almondSyrup] false : X / true : O
 * [vanillaSyrup] false : X / true : O
 * **/


//--------------------------------------------------------------query test
show databases;
show tables;

SELECT * FROM order_each WHERE order_main_code=100001;
SELECT * FROM order_each WHERE order_main_code=100001 AND product_code=3000;

/*order_main을 지우면 order_main을 참조하는 receipt_main과 order_each도 사라진다.*/
DELETE FROM order_main WHERE orderCode=100002;
SELECT * FROM order_each WHERE order_main_code=100002;
SELECT * FROM receipt_main WHERE order_main_code=100002;

