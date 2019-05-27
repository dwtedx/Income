package com.dwtedx.income.sqliteservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;

import java.util.Calendar;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String nowTime = CommonUtility.getCurrentTime();

	private static final String createIncomeSql = "create table di_income(" +
			"id integer primary key autoincrement," +
			"username varchar(500) null," +
			"userid integer null," +
			"role integer null," +
			"moneysum decimal(18,2) null," +
			"type varchar(500) null," +
			"typeid integer null," +
			"account varchar(50) null," +
			"accountid integer null," +
			"remark varchar(1000) null, " +
			"location varchar(500) null, " +
			"voicepath varchar(500) null," +
			"imagepath varchar(500) null," +
			"recordtype integer null," +
			"recordtime varchar(500) null," +
			"createtime varchar(500) null," +
			"updatetime varchar(500) null," +
			"isupdate integer null," +
			"serverid integer null," +
			"deletefalag integer null);";

	private static final String createTypeSql = "create table di_type(" +
			"id integer primary key autoincrement," +
			"username varchar(500) null," +
			"userid integer null," +
			"name varchar(500) null," +
			"type integer null," +
			"icon varchar(500) null," +
			"color varchar(500) null," +
			"sequence integer null," +
			"remark varchar(1000) null," +
			"createtime datetime null," +
			"updatetime datetime null," +
			"serverid integer null," +
			"deletefalag integer null);";

	private static final String createAccountSql = "create table di_account(" +
			"id integer primary key autoincrement," +
			"moneysum decimal(18,2) null," +
			"username varchar(500) null," +
			"userid integer null," +
			"name varchar(500) null," +
			"type integer null," +
			"icon varchar(500) null," +
			"color varchar(500) null," +
			"sequence integer null," +
			"remark varchar(1000) null, " +
			"createtime datetime null," +
			"updatetime datetime null," +
			"serverid integer null," +
			"deletefalag integer null);";



	/**
	 * 版本2 type account 表添加serverid
	 */
	private static final String alterTypeSql = "alter table di_type add serverid integer;";
	private static final String alterAccountSql = "alter table di_account add serverid integer;";

	/**
	 * 版本3 添加预算表
	 */
	private static final String createBudgetSql = "create table di_budget(" +
			"id integer primary key autoincrement," +
			"moneysum decimal(18,2) null," +
			"moneylast decimal(18,2) null," +
			"username varchar(500) null," +
			"userid integer null," +
			"yearnom integer null," +
			"monthnom integer null," +
			"remark varchar(1000) null, " +
			"createtime datetime null," +
			"updatetime datetime null," +
			"isupdate integer null);";

	/**
	 * 版本4 type account 表添加deletefalag int 1 表示删除
	 */
	private static final String alterTypeSql_deletefalag = "alter table di_type add deletefalag integer;";
	private static final String alterAccountSql_deletefalag = "alter table di_account add deletefalag integer;";
	private static final String alterTypeSql_deletefalag_set = "update di_type set deletefalag = 0;";
	private static final String alterAccountSql_deletefalag_set = "update di_account set deletefalag = 0;";

	/**
	 * 版本5 income 表添加serverid int 1 表示服务器id
	 */
	private static final String alterIncomeSql = "alter table di_income add serverid integer;";

	/**
	 * 版本6 新增扫单的表 和 income recordtype 字段 0：表示默认 1：扫单类型
	 * deletefalag 1 表示删除
	 * @param context
	 */
	private static final String alterIncomeRecordTypeSql = "alter table di_income add recordtype integer;";
	private static final String alterIncomeSql_deletefalag = "alter table di_income add deletefalag integer;";
	private static final String alterIncomeSql_deletefalag_set = "update di_income set deletefalag = 0;";
	private static final String createScanSql = "create table di_scan(" +
			"id integer primary key autoincrement," +
			"username varchar(500) null," +
			"userid integer null," +
			"incomeid integer null," +
			"moneysum decimal(18,2) null," +
			"name varchar(500) null," +
			"store varchar(500) null," +
			"brand varchar(500) null," +
			"quantity varchar(500) null," +
			"type integer null," +
			"sequence integer null," +
			"remark varchar(1000) null," +
			"createtime datetime null," +
			"updatetime datetime null," +
			"serverid integer null," +
			"deletefalag integer null," +
			"isupdate integer null);";

	/**
	 * 版本7 income location varchar(500) null 表示记账位置
	 */
	private static final String alterIncomeSql7 = "alter table di_income add location varchar(500);";

	public DBOpenHelper(Context context) {
		//初始化
		super(context, "income.db", null, 7);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//是在数据库每一次被创建的时候调用的
		db.execSQL(createIncomeSql);
		db.execSQL(createAccountSql);
		db.execSQL(createTypeSql);
		db.execSQL(createBudgetSql);
		db.execSQL(createScanSql);

		//支出类型
		for (int i = 1; i <= CommonConstants.PAYTYPE_NAME.length; i++) {
			db.execSQL("INSERT INTO di_type (id, username, userid, name, type, icon, color, sequence, remark, createtime, updatetime, serverid, deletefalag) VALUES (" + i + ", 'sys', 0, '" + CommonConstants.PAYTYPE_NAME[i-1] + "', " + CommonConstants.INCOME_ROLE_PAYING + ", '" + CommonConstants.PAYTYPE_ICON[i-1] + "', '" + CommonConstants.PAYTYPE_ICON_COLOR[i-1] + "', " + i + ", 'init', '" + nowTime + "', '" + nowTime + "', " + i + ", 0);");
		}
		//收入类型
		for (int i = 1; i <= CommonConstants.INCOMETYPE_NAME.length; i++) {
			db.execSQL("INSERT INTO di_type (id, username, userid, name, type, icon, color, sequence, remark, createtime, updatetime, serverid, deletefalag) VALUES (" + (i + CommonConstants.PAYTYPE_NAME.length) + ", 'sys', 0, '" + CommonConstants.INCOMETYPE_NAME[i-1] + "', " + CommonConstants.INCOME_ROLE_INCOME + ", '" + CommonConstants.INCOMETYPE_ICON[i-1] + "', '" + CommonConstants.INCOMETYPE_ICON_COLOR[i-1] + "', " + (i + CommonConstants.PAYTYPE_NAME.length) + ", 'init', '" + nowTime + "', '" + nowTime + "', " + (i + CommonConstants.PAYTYPE_NAME.length) + ", 0);");
		}

		//帐户类型
		for (int i = 1; i <= CommonConstants.ACCOUNT_NAME.length; i++) {
			db.execSQL("INSERT INTO di_account (id, moneysum, username, userid, name, type, icon, color, sequence, remark, createtime, updatetime, serverid, deletefalag) VALUES (" + i + ", 0.00, 'sys', 0, '" + CommonConstants.ACCOUNT_NAME[i - 1] + "', " + CommonConstants.INCOME_ACCOUNT_TYPE_SYS +", '" + CommonConstants.ACCOUNT_ICON[i - 1] + "', '" + CommonConstants.ACCOUNT_ICON_COLOR[i - 1] + "', " + i + ", 'init', '" + nowTime + "', '" + nowTime + "', " + i + ", 0);");
		}

		//预算
		Calendar ca = Calendar.getInstance();
		db.execSQL("INSERT INTO di_budget (id, moneysum, moneylast, username, userid, yearnom, monthnom, remark, createtime, updatetime, isupdate) VALUES (1, 3000.00, 3000.00, null, 0, " + ca.get(Calendar.YEAR) + ", " + (ca.get(Calendar.MONTH) + 1) + ", 'init', '" + nowTime + "', '" + nowTime + "', " + CommonConstants.INCOME_RECORD_NOT_UPDATE + ");");
	}

	/**
	 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/6/29 下午1:51.
	 * Company 路之遥网络科技有限公司
	 * Description //当打开数据库时传入的版本号与当前的版本号不同时会调用该方法
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//版本2222222222222222222222222222222222222222222222222222222222
		if(1 == oldVersion && 2 == newVersion) {
			onUpgrade2(db);
		}
		//版本3333333333333333333333333333333333333333333333333333333333333333333333333
		if(1 == oldVersion && 3 == newVersion) {
			onUpgrade2(db);
			//3
			onUpgrade3(db);

		}
		if(2 == oldVersion && 3 == newVersion) {
			//3
			onUpgrade3(db);

		}
		//版本444444444444444444444444444444444444444444444444444444444444
		if(1 == oldVersion && 4 == newVersion) {
			onUpgrade2(db);
			//3
			onUpgrade3(db);
			//4
			onUpgrade4(db);
		}
		if(2 == oldVersion && 4 == newVersion) {
			//3
			onUpgrade3(db);
			//4
			onUpgrade4(db);
		}
		if(3 == oldVersion && 4 == newVersion) {
			//4
			onUpgrade4(db);
		}

		//版本55555555555555555555555555555555555555555555555555555555
		if(1 == oldVersion && 5 == newVersion) {
			onUpgrade2(db);
			//3
			onUpgrade3(db);
			//4
			onUpgrade4(db);
			//5
			onUpgrade5(db);
		}
		if(2 == oldVersion && 5 == newVersion) {
			//3
			onUpgrade3(db);
			//4
			onUpgrade4(db);
			//5
			onUpgrade5(db);
		}
		if(3 == oldVersion && 5 == newVersion) {
			//4
			onUpgrade4(db);
			//5
			onUpgrade5(db);
		}
		if(4 == oldVersion && 5 == newVersion) {
			//5
			onUpgrade5(db);
		}

		//版本66666666666666666666666666666666666666666666666
		if(1 == oldVersion && 6 == newVersion) {
			onUpgrade2(db);
			//3
			onUpgrade3(db);
			//4
			onUpgrade4(db);
			//5
			onUpgrade5(db);
			//6
			onUpgrade6(db);
		}
		if(2 == oldVersion && 6 == newVersion) {
			//3
			onUpgrade3(db);
			//4
			onUpgrade4(db);
			//5
			onUpgrade5(db);
			//6
			onUpgrade6(db);
		}
		if(3 == oldVersion && 6 == newVersion) {
			//4
			onUpgrade4(db);
			//5
			onUpgrade5(db);
			//6
			onUpgrade6(db);
		}
		if(4 == oldVersion && 6 == newVersion) {
			//5
			onUpgrade5(db);
			//6
			onUpgrade6(db);
		}
		if(5 == oldVersion && 6 == newVersion) {
			//6
			onUpgrade6(db);
		}

		//版本77777777777777777777777777777777777777777777777777777777777777777
		if(1 == oldVersion && 7 == newVersion) {
			onUpgrade2(db);
			//3
			onUpgrade3(db);
			//4
			onUpgrade4(db);
			//5
			onUpgrade5(db);
			//6
			onUpgrade6(db);
			//7
			onUpgrade7(db);
		}
		if(2 == oldVersion && 7 == newVersion) {
			//3
			onUpgrade3(db);
			//4
			onUpgrade4(db);
			//5
			onUpgrade5(db);
			//6
			onUpgrade6(db);
			//7
			onUpgrade7(db);
		}
		if(3 == oldVersion && 7 == newVersion) {
			//4
			onUpgrade4(db);
			//5
			onUpgrade5(db);
			//6
			onUpgrade6(db);
			//7
			onUpgrade7(db);
		}
		if(4 == oldVersion && 7 == newVersion) {
			//5
			onUpgrade5(db);
			//6
			onUpgrade6(db);
			//7
			onUpgrade7(db);
		}
		if(5 == oldVersion && 7 == newVersion) {
			//6
			onUpgrade6(db);
			//7
			onUpgrade7(db);
		}
		if(6 == oldVersion && 7 == newVersion) {
			//7
			onUpgrade7(db);
		}
	}

	private void onUpgrade7(SQLiteDatabase db) {
		db.execSQL(alterIncomeSql7);
	}

	private void onUpgrade6(SQLiteDatabase db) {
		db.execSQL(createScanSql);
		db.execSQL(alterIncomeRecordTypeSql);
		db.execSQL(alterIncomeSql_deletefalag);
		db.execSQL(alterIncomeSql_deletefalag_set);
	}

	private void onUpgrade5(SQLiteDatabase db) {
		db.execSQL(alterIncomeSql);
	}

	private void onUpgrade4(SQLiteDatabase db) {
		db.execSQL(alterTypeSql_deletefalag);
		db.execSQL(alterAccountSql_deletefalag);
		db.execSQL(alterTypeSql_deletefalag_set);
		db.execSQL(alterAccountSql_deletefalag_set);
	}

	private void onUpgrade3(SQLiteDatabase db) {
		db.execSQL(createBudgetSql);
		//预算
		Calendar ca = Calendar.getInstance();
		db.execSQL("INSERT INTO di_budget (id, moneysum, moneylast, username, userid, yearnom, monthnom, remark, createtime, updatetime, isupdate) VALUES (1, 3000.00, 3000.00, null, 0, " + ca.get(Calendar.YEAR) + ", " + (ca.get(Calendar.MONTH) + 1) + ", 'init', '" + nowTime + "', '" + nowTime + "', " + CommonConstants.INCOME_RECORD_NOT_UPDATE + ");");
	}

	private void onUpgrade2(SQLiteDatabase db) {
		db.execSQL(alterTypeSql);
		db.execSQL(alterAccountSql);
	}

}
