package com.dwtedx.income.sqliteservice;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dwtedx.income.entity.DiIncome;
import com.dwtedx.income.provider.RecordFirstSharedPreferences;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DlIncomeService {

	private static DlIncomeService mService = null;
	private DBOpenHelper dbOpenHelper;
	private Context mContext;

	private DlIncomeService(Context context) {
		this.mContext = context;
		this.dbOpenHelper = new DBOpenHelper(context);
	}

	public static DlIncomeService getInstance(Context context) {
		if (mService == null)
			mService = new DlIncomeService(context);
		return mService;
	}
	
	/**
	 * 添加记录
	 * @param income
	 */
	public void save(DiIncome income){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		//如果是第一次插入、添加一条时间轴开始节点
		RecordFirstSharedPreferences.init(mContext);
		if(RecordFirstSharedPreferences.getIsFirstAdd()){
			if(0 == this.getCount()) {
				Date createTime = CommonUtility.stringToDate(income.getRecordtime());
				Calendar date = Calendar.getInstance();
				date.setTime(createTime);
				date.set(Calendar.MINUTE, date.get(Calendar.MINUTE) - 1);
				String dateEnd = CommonUtility.stringDateFormart(date.getTime());
				db.execSQL("INSERT INTO di_income (role, recordtime, createtime, updatetime, isupdate, deletefalag) VALUES (?, ?, ?, ?, ?, ?)",
						new Object[]{CommonConstants.INCOME_ROLE_START, dateEnd, dateEnd, dateEnd, income.getIsupdate(), income.getDeletefalag()});
			}
			RecordFirstSharedPreferences.setIsFirstAdd(false);
		}
		db.execSQL("INSERT INTO di_income (username, userid, role, moneysum, type, typeid, account, accountid, remark, location, voicepath, imagepath, recordtype, recordtime, createtime, updatetime, isupdate, serverid, deletefalag) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new Object[]{income.getUsername(),
						income.getUserid(), income.getRole(), income.getMoneysum(),
						income.getType(), income.getTypeid(),
						income.getAccount(), income.getAccountid(),
						income.getRemark(), income.getLocation(), income.getVoicepath(),
						income.getImagepath(), income.getRecordtype(), income.getRecordtime(),
						income.getCreatetime(), income.getUpdatetime(), income.getIsupdate(), income.getServerid(), income.getDeletefalag()});
	}

	/**
	 * 同步
	 * @param income
	 */
	public void saveSynchronizeEgain(DiIncome income){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		//如果是第一次插入、添加一条时间轴开始节点
		db.execSQL("INSERT INTO di_income (username, userid, role, moneysum, type, typeid, account, accountid, remark, location, voicepath, imagepath, recordtype, recordtime, createtime, updatetime, isupdate, serverid, deletefalag) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new Object[]{income.getUsername(),
						income.getUserid(), income.getRole(), income.getMoneysum(),
						income.getType(), income.getTypeid(),
						income.getAccount(), income.getAccountid(),
						income.getRemark(), income.getLocation(), income.getVoicepath(),
						income.getImagepath(), income.getRecordtype(),income.getRecordtime(),
						income.getCreatetime(), income.getUpdatetime(), income.getIsupdate(), income.getId(), income.getDeletefalag()});
	}
	
	/**
	 * 删除记录 逻辑删除
	 * @param id 记录ID
	 */
	public void delete(Integer id){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("UPDATE di_income set deletefalag = 1 WHERE id = ?", new Object[]{id});
	}

	/**
	 * 删除记录
	 */
	public void deleteAll(){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("DELETE FROM di_income WHERE 1 = 1");
		db.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE name = ?", new Object[]{"di_income"});
	}

	/**
	 * 更新记录
	 * @param income
	 */
	public void update(DiIncome income){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("UPDATE di_income SET username=?, userid=?, role=?, moneysum=?, type=?, typeid=?, account=?, accountid=?, remark=?, location=?, voicepath=?, imagepath=?, recordtype=?, recordtime=?, createtime=?, updatetime=?, isupdate=?, serverid=?, deletefalag=? WHERE id=?",
				new Object[]{income.getUsername(),
						income.getUserid(), income.getRole(), income.getMoneysum(),
						income.getType(), income.getTypeid(),
						income.getAccount(), income.getAccountid(),
						income.getRemark(), income.getLocation(), income.getVoicepath(),
						income.getImagepath(), income.getRecordtype(), income.getRecordtime(),
						income.getCreatetime(), income.getUpdatetime(), income.getIsupdate(), income.getServerid(), income.getDeletefalag(), income.getId()});
	}

	/**
	 * 查询记录
	 * @param id 记录ID
	 * @return
	 */
	public DiIncome find(Integer id){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, username, userid, role, moneysum, type, typeid, account, accountid, remark, location, voicepath, imagepath, recordtype, recordtime, createtime, updatetime, isupdate, serverid, deletefalag FROM di_income WHERE id=?", new String[]{id.toString()});
		if(cursor.moveToFirst()){
			return new DiIncome(
				cursor.getInt(cursor.getColumnIndex("id")),
				cursor.getString(cursor.getColumnIndex("username")),
				cursor.getInt(cursor.getColumnIndex("userid")),
				cursor.getInt(cursor.getColumnIndex("role")),
				cursor.getDouble(cursor.getColumnIndex("moneysum")),
				cursor.getString(cursor.getColumnIndex("type")),
				cursor.getInt(cursor.getColumnIndex("typeid")),
				cursor.getString(cursor.getColumnIndex("account")),
				cursor.getInt(cursor.getColumnIndex("accountid")),
				cursor.getString(cursor.getColumnIndex("remark")),
				cursor.getString(cursor.getColumnIndex("location")),
				cursor.getString(cursor.getColumnIndex("voicepath")),
				cursor.getString(cursor.getColumnIndex("imagepath")),
				cursor.getInt(cursor.getColumnIndex("recordtype")),
				cursor.getString(cursor.getColumnIndex("recordtime")),
				cursor.getString(cursor.getColumnIndex("createtime")),
				cursor.getString(cursor.getColumnIndex("updatetime")),
				cursor.getInt(cursor.getColumnIndex("isupdate")),
				cursor.getInt(cursor.getColumnIndex("serverid")),
				cursor.getInt(cursor.getColumnIndex("deletefalag"))
			);
		}
		cursor.close();
		return null;
	}

	/**
	 * 查询记录befortime
	 * @return
	 */
	public DiIncome findBeForTime(){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT recordtime, isupdate FROM di_income where role = 3 LIMIT 1", null);
		if(cursor.moveToFirst()){
			return  new DiIncome(cursor.getString(cursor.getColumnIndex("recordtime")), cursor.getInt(cursor.getColumnIndex("isupdate")));
		}
		cursor.close();
		return null;
	}

	/**
	 * 更新开始节点的时间
	 * @return
	 */
	public void updateBeForTime(String time){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("UPDATE di_income set recordtime = ? WHERE role = ?", new Object[]{time, 3});
	}

	/**
	 * 查询记录
	 * @return
	 */
	public List<DiIncome> findNotUpdate(){
		List<DiIncome> incomes = new ArrayList<DiIncome>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, username, userid, role, moneysum, type, typeid, account, accountid, remark, location, voicepath, imagepath, recordtype, recordtime, createtime, updatetime, isupdate, serverid, deletefalag FROM di_income WHERE isupdate=" + CommonConstants.INCOME_RECORD_NOT_UPDATE + " LIMIT 0,20", null);
		DiIncome income;
		while(cursor.moveToNext()){
			income = new DiIncome(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getString(cursor.getColumnIndex("username")),
					cursor.getInt(cursor.getColumnIndex("userid")),
					cursor.getInt(cursor.getColumnIndex("role")),
					cursor.getDouble(cursor.getColumnIndex("moneysum")),
					cursor.getString(cursor.getColumnIndex("type")),
					cursor.getInt(cursor.getColumnIndex("typeid")),
					cursor.getString(cursor.getColumnIndex("account")),
					cursor.getInt(cursor.getColumnIndex("accountid")),
					cursor.getString(cursor.getColumnIndex("remark")),
					cursor.getString(cursor.getColumnIndex("location")),
					cursor.getString(cursor.getColumnIndex("voicepath")),
					cursor.getString(cursor.getColumnIndex("imagepath")),
					cursor.getInt(cursor.getColumnIndex("recordtype")),
					cursor.getString(cursor.getColumnIndex("recordtime")),
					cursor.getString(cursor.getColumnIndex("createtime")),
					cursor.getString(cursor.getColumnIndex("updatetime")),
					cursor.getInt(cursor.getColumnIndex("isupdate")),
					cursor.getInt(cursor.getColumnIndex("serverid")),
					cursor.getInt(cursor.getColumnIndex("deletefalag"))
			);
			if(CommonConstants.INCOME_RECORD_TYPE_1 == income.getRecordtype()){
				income.setScanList(DIScanService.getInstance(mContext).findByIncomeId(income.getId()));
			}
			incomes.add(income);
		}
		cursor.close();
		return incomes;
	}

	/**
	 * 分页获取记录
	 * @param offset 跳过前面多少条记录
	 * @param maxResult 每页获取多少条记录
	 * @return
	 */
	public List<DiIncome> getScrollData(int offset, int maxResult){
		List<DiIncome> incomes = new ArrayList<DiIncome>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT a.id, a.username, a.userid, a.role, a.moneysum, a.type, a.typeid, a.account, a.accountid, a.remark, a.location, a.voicepath, a.imagepath, a.recordtype, a.recordtime, a.createtime, a.updatetime, a.isupdate, a.serverid, a.deletefalag, b.color, b.icon FROM di_income a left join di_type b on a.typeid = b.id WHERE a.deletefalag = 0 ORDER BY a.recordtime DESC LIMIT ?,?",
				new String[]{String.valueOf(offset), String.valueOf(maxResult)});

		while(cursor.moveToNext()){
			incomes.add(new DiIncome(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getString(cursor.getColumnIndex("username")),
					cursor.getInt(cursor.getColumnIndex("userid")),
					cursor.getInt(cursor.getColumnIndex("role")),
					cursor.getDouble(cursor.getColumnIndex("moneysum")),
					cursor.getString(cursor.getColumnIndex("type")),
					cursor.getInt(cursor.getColumnIndex("typeid")),
					cursor.getString(cursor.getColumnIndex("account")),
					cursor.getInt(cursor.getColumnIndex("accountid")),
					cursor.getString(cursor.getColumnIndex("remark")),
					cursor.getString(cursor.getColumnIndex("location")),
					cursor.getString(cursor.getColumnIndex("voicepath")),
					cursor.getString(cursor.getColumnIndex("imagepath")),
					cursor.getInt(cursor.getColumnIndex("recordtype")),
					cursor.getString(cursor.getColumnIndex("recordtime")),
					cursor.getString(cursor.getColumnIndex("createtime")),
					cursor.getString(cursor.getColumnIndex("updatetime")),
					cursor.getInt(cursor.getColumnIndex("isupdate")),
					cursor.getInt(cursor.getColumnIndex("serverid")),
					cursor.getInt(cursor.getColumnIndex("deletefalag")),
					cursor.getString(cursor.getColumnIndex("color")),
					cursor.getString(cursor.getColumnIndex("icon"))
			));
		}
		cursor.close();
		return incomes;
	}


	/**
	 * 分页获取记录By accountid
	 * @param offset 跳过前面多少条记录
	 * @param maxResult 每页获取多少条记录
	 * @return
	 */
	public List<DiIncome> getScrollDataByAccountId(int accountid, int offset, int maxResult){
		List<DiIncome> incomes = new ArrayList<DiIncome>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT a.id, a.username, a.userid, a.role, a.moneysum, a.type, a.typeid, a.account, a.accountid, a.remark, a.location, a.voicepath, a.imagepath, a.recordtype, a.recordtime, a.createtime, a.updatetime, a.isupdate, a.serverid, a.deletefalag, b.color, b.icon FROM di_income a left join di_type b on a.typeid = b.id WHERE a.deletefalag = 0 AND a.accountid = ? ORDER BY a.recordtime DESC LIMIT ?,?",
				new String[]{String.valueOf(accountid), String.valueOf(offset), String.valueOf(maxResult)});

		while(cursor.moveToNext()){
			incomes.add(new DiIncome(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getString(cursor.getColumnIndex("username")),
					cursor.getInt(cursor.getColumnIndex("userid")),
					cursor.getInt(cursor.getColumnIndex("role")),
					cursor.getDouble(cursor.getColumnIndex("moneysum")),
					cursor.getString(cursor.getColumnIndex("type")),
					cursor.getInt(cursor.getColumnIndex("typeid")),
					cursor.getString(cursor.getColumnIndex("account")),
					cursor.getInt(cursor.getColumnIndex("accountid")),
					cursor.getString(cursor.getColumnIndex("remark")),
					cursor.getString(cursor.getColumnIndex("location")),
					cursor.getString(cursor.getColumnIndex("voicepath")),
					cursor.getString(cursor.getColumnIndex("imagepath")),
					cursor.getInt(cursor.getColumnIndex("recordtype")),
					cursor.getString(cursor.getColumnIndex("recordtime")),
					cursor.getString(cursor.getColumnIndex("createtime")),
					cursor.getString(cursor.getColumnIndex("updatetime")),
					cursor.getInt(cursor.getColumnIndex("isupdate")),
					cursor.getInt(cursor.getColumnIndex("serverid")),
					cursor.getInt(cursor.getColumnIndex("deletefalag")),
					cursor.getString(cursor.getColumnIndex("color")),
					cursor.getString(cursor.getColumnIndex("icon"))
			));
		}
		cursor.close();
		return incomes;
	}

	/**
	 * 分页获取记录By accountid
	 * @param offset 跳过前面多少条记录
	 * @param maxResult 每页获取多少条记录
	 * @return
	 */
	public List<DiIncome> getScrollDataByAccountId(int accountid, String startTime, String endTime, int offset, int maxResult){
		List<DiIncome> incomes = new ArrayList<DiIncome>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT a.id, a.username, a.userid, a.role, a.moneysum, a.type, a.typeid, a.account, a.accountid, a.remark, a.location, a.voicepath, a.imagepath, a.recordtype, a.recordtime, a.createtime, a.updatetime, a.isupdate, a.serverid, a.deletefalag, b.color, b.icon FROM di_income a left join di_type b on a.typeid = b.id WHERE a.deletefalag = 0 AND a.accountid = ? AND a.recordtime >= ? AND a.recordtime <= ? ORDER BY a.recordtime DESC LIMIT ?,?",
				new String[]{String.valueOf(accountid), String.valueOf(startTime), String.valueOf(endTime), String.valueOf(offset), String.valueOf(maxResult)});

		while(cursor.moveToNext()){
			incomes.add(new DiIncome(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getString(cursor.getColumnIndex("username")),
					cursor.getInt(cursor.getColumnIndex("userid")),
					cursor.getInt(cursor.getColumnIndex("role")),
					cursor.getDouble(cursor.getColumnIndex("moneysum")),
					cursor.getString(cursor.getColumnIndex("type")),
					cursor.getInt(cursor.getColumnIndex("typeid")),
					cursor.getString(cursor.getColumnIndex("account")),
					cursor.getInt(cursor.getColumnIndex("accountid")),
					cursor.getString(cursor.getColumnIndex("remark")),
					cursor.getString(cursor.getColumnIndex("location")),
					cursor.getString(cursor.getColumnIndex("voicepath")),
					cursor.getString(cursor.getColumnIndex("imagepath")),
					cursor.getInt(cursor.getColumnIndex("recordtype")),
					cursor.getString(cursor.getColumnIndex("recordtime")),
					cursor.getString(cursor.getColumnIndex("createtime")),
					cursor.getString(cursor.getColumnIndex("updatetime")),
					cursor.getInt(cursor.getColumnIndex("isupdate")),
					cursor.getInt(cursor.getColumnIndex("serverid")),
					cursor.getInt(cursor.getColumnIndex("deletefalag")),
					cursor.getString(cursor.getColumnIndex("color")),
					cursor.getString(cursor.getColumnIndex("icon"))
			));
		}
		cursor.close();
		return incomes;
	}


	/**
	 * 记录时间获取记录
	 * @param startTime 形如时间
	 * @param endTime 结束时间
	 * @return
	 */
	public List<DiIncome> getByRecordtimeData(String startTime, String endTime){
		List<DiIncome> incomes = new ArrayList<DiIncome>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select id, username, userid, role, moneysum, type, typeid, account, accountid, remark, location, voicepath, imagepath, recordtype, recordtime, createtime, updatetime, isupdate, serverid, deletefalag FROM di_income a WHERE a.deletefalag = 0 AND a.recordtime >= ? AND a.recordtime <= ?;",
				new String[]{String.valueOf(startTime), String.valueOf(endTime)});

		while(cursor.moveToNext()){
			incomes.add(new DiIncome(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getString(cursor.getColumnIndex("username")),
					cursor.getInt(cursor.getColumnIndex("userid")),
					cursor.getInt(cursor.getColumnIndex("role")),
					cursor.getDouble(cursor.getColumnIndex("moneysum")),
					cursor.getString(cursor.getColumnIndex("type")),
					cursor.getInt(cursor.getColumnIndex("typeid")),
					cursor.getString(cursor.getColumnIndex("account")),
					cursor.getInt(cursor.getColumnIndex("accountid")),
					cursor.getString(cursor.getColumnIndex("remark")),
					cursor.getString(cursor.getColumnIndex("location")),
					cursor.getString(cursor.getColumnIndex("voicepath")),
					cursor.getString(cursor.getColumnIndex("imagepath")),
					cursor.getInt(cursor.getColumnIndex("recordtype")),
					cursor.getString(cursor.getColumnIndex("recordtime")),
					cursor.getString(cursor.getColumnIndex("createtime")),
					cursor.getString(cursor.getColumnIndex("updatetime")),
					cursor.getInt(cursor.getColumnIndex("isupdate")),
					cursor.getInt(cursor.getColumnIndex("serverid")),
					cursor.getInt(cursor.getColumnIndex("deletefalag"))
			));
		}
		cursor.close();
		return incomes;
	}

	/**
	 * 统计图记录时间获取记录
	 * @param startTime 形如时间
	 * @param endTime 结束时间
	 * @return
	 */
	public List<DiIncome> getByTypeSumMoneyData(int role, String startTime, String endTime){
		List<DiIncome> incomes = new ArrayList<DiIncome>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select sum(a.moneysum) as allmoneysum, a.role, a.type, a.typeid, b.color, a.recordtime FROM di_income a left join di_type b on a.typeid = b.id WHERE a.deletefalag = 0 AND a.role = ? and a.recordtime >= ? and a.recordtime <= ? group by a.type;",
				new String[]{String.valueOf(role), String.valueOf(startTime), String.valueOf(endTime)});

		while(cursor.moveToNext()){
			incomes.add(new DiIncome(
					cursor.getDouble(cursor.getColumnIndex("allmoneysum")),
					cursor.getInt(cursor.getColumnIndex("role")),
					cursor.getString(cursor.getColumnIndex("type")),
					cursor.getInt(cursor.getColumnIndex("typeid")),
					cursor.getString(cursor.getColumnIndex("color")),
					cursor.getString(cursor.getColumnIndex("recordtime"))
			));
		}
		cursor.close();
		return incomes;
	}

	/**
	 * 统计图记录两个日期之间的和
	 * @param startTime 形如时间
	 * @param endTime 结束时间
	 * @return
	 */
	public DiIncome getSumMoneyByData(int role, String startTime, String endTime){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select sum(a.moneysum) as allmoneysum FROM di_income a WHERE a.deletefalag = 0 AND a.role = ? AND a.recordtime >= ? AND a.recordtime <= ?;",
				new String[]{String.valueOf(role), String.valueOf(startTime), String.valueOf(endTime)});

		if(cursor.moveToFirst()) {
			return new DiIncome(
					cursor.getDouble(cursor.getColumnIndex("allmoneysum"))
			);
		}
		cursor.close();
		return null;

	}

	/**
	 * 统计图记录两个日期之间的和
	 * @param startTime 形如时间
	 * @param endTime 结束时间
	 * @return
	 */
	public DiIncome getSumMoneyByDataAccount(int role, int account, String startTime, String endTime){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select sum(a.moneysum) as allmoneysum FROM di_income a WHERE a.deletefalag = 0 AND a.role = ? AND a.accountid = ? AND a.recordtime >= ? AND a.recordtime <= ?;",
				new String[]{String.valueOf(role), String.valueOf(account), String.valueOf(startTime), String.valueOf(endTime)});

		if(cursor.moveToFirst()) {
			return new DiIncome(
					cursor.getDouble(cursor.getColumnIndex("allmoneysum"))
			);
		}
		cursor.close();
		return null;

	}

	/**
	 * 获取记录总数
	 * @return
	 */
	public long getCount(){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM di_income WHERE deletefalag = 0", null);
		cursor.moveToFirst();
		long result = cursor.getLong(0);
		cursor.close();
		return result;
	}
	
	/**
	 * 搜索获取记录
	 * @return
	 */
	public List<DiIncome> query(String searchtext, int offset, int maxResult){
		List<DiIncome> incomes = new ArrayList<DiIncome>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, username, userid, role, moneysum, type, typeid, account, accountid, remark, location, voicepath, imagepath, recordtype, recordtime, createtime, updatetime, isupdate, serverid, deletefalag FROM di_income WHERE deletefalag = 0 AND moneysum = '" + searchtext + "' OR type LIKE '%" + searchtext + "%' OR account LIKE '%" + searchtext + "%' OR remark LIKE '%" + searchtext + "%' ORDER BY recordtime DESC LIMIT ?,?",
				new String[]{String.valueOf(offset), String.valueOf(maxResult)});
		while(cursor.moveToNext()){
			incomes.add(new DiIncome(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getString(cursor.getColumnIndex("username")),
					cursor.getInt(cursor.getColumnIndex("userid")),
					cursor.getInt(cursor.getColumnIndex("role")),
					cursor.getDouble(cursor.getColumnIndex("moneysum")),
					cursor.getString(cursor.getColumnIndex("type")),
					cursor.getInt(cursor.getColumnIndex("typeid")),
					cursor.getString(cursor.getColumnIndex("account")),
					cursor.getInt(cursor.getColumnIndex("accountid")),
					cursor.getString(cursor.getColumnIndex("remark")),
					cursor.getString(cursor.getColumnIndex("location")),
					cursor.getString(cursor.getColumnIndex("voicepath")),
					cursor.getString(cursor.getColumnIndex("imagepath")),
					cursor.getInt(cursor.getColumnIndex("recordtype")),
					cursor.getString(cursor.getColumnIndex("recordtime")),
					cursor.getString(cursor.getColumnIndex("createtime")),
					cursor.getString(cursor.getColumnIndex("updatetime")),
					cursor.getInt(cursor.getColumnIndex("isupdate")),
					cursor.getInt(cursor.getColumnIndex("serverid")),
					cursor.getInt(cursor.getColumnIndex("deletefalag"))
			));
		}
		cursor.close();
		return incomes;
	}

	/**
	 * 获取最后一个id
	 * @return
	 */
	public int getLastId(){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select last_insert_rowid()", null);
		cursor.moveToFirst();
		int result = cursor.getInt(0);
		cursor.close();
		return result;
	}

}
