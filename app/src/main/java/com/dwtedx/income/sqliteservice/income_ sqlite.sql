/** 收入表 */
DROP TABLE IF EXISTS di_income;
create table di_income(
	id integer primary key autoincrement,
	username varchar(500) null,
	userid integer null,
	role integer null,
	moneysum decimal(18,2) null,
	type varchar(500) null,
	typeid integer null,
	account varchar(50) null,
	accountid integer null,
	remark varchar(1000) null, 
	voicepath varchar(500) null,
	imagepath varchar(500) null,
	recordtime datetime null,
	createtime datetime null,
	updatetime datetime null,
	isupdate integer null
);

/** 类型表 */
DROP TABLE IF EXISTS di_type;
create table di_type(
	id integer primary key autoincrement,
	username varchar(500) null,
    userid integer null,
	name varchar(500) null,
	type integer null,
	icon varchar(500) null,
	color varchar(500) null,
	sequence integer null,
	remark varchar(1000) null,
	createtime datetime null,
	updatetime datetime null
);

/** 帐户表 */
DROP TABLE IF EXISTS di_account;
create table di_account(
	id integer primary key autoincrement,
	moneysum decimal(18,2) null,
	username varchar(500) null,
    userid integer null,
	name varchar(500) null,
	type integer null,
	icon varchar(500) null,
	color varchar(500) null,
	sequence integer null,
	remark varchar(1000) null,
	createtime datetime null,
	updatetime datetime null
);