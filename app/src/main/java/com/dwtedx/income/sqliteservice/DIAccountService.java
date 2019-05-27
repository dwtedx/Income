package com.dwtedx.income.sqliteservice;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dwtedx.income.entity.DiAccount;
import com.dwtedx.income.entity.DiType;
import com.dwtedx.income.provider.RecordFirstSharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class DIAccountService {

	private static DIAccountService mService = null;
	private DBOpenHelper dbOpenHelper;
	private Context mContext;

	private DIAccountService(Context context) {
		this.mContext = context;
		this.dbOpenHelper = new DBOpenHelper(context);
	}

	public static DIAccountService getInstance(Context context) {
		if (mService == null)
			mService = new DIAccountService(context);
		return mService;
	}
	
	/**
	 * 添加记录
	 * @param account
	 */
	public void save(DiAccount account){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		//如果是第一次插入、添加一条时间轴开始节点
		RecordFirstSharedPreferences.init(mContext);
		db.execSQL("INSERT INTO di_account (moneysum, username, userid, name, type, icon, color, sequence, remark, createtime, updatetime, serverid, deletefalag) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new Object[]{account.getMoneysum(),
						account.getUsername(),
						account.getUserid(),
						account.getName(),
						account.getType(),
						account.getIcon(),
						account.getColor(),
						account.getSequence(),
						account.getRemark(),
						account.getCreatetime(),
						account.getUpdatetime(),
						account.getServerid(),
						account.getDeletefalag()});
	}

	/**
	 * 同步
	 * @param account
	 */
	public void saveSynchronizeEgain(DiAccount account){
		DiAccount account1 = this.find(account.getName());
		if(null == account1) {
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			//如果是第一次插入、添加一条时间轴开始节点
			RecordFirstSharedPreferences.init(mContext);
			db.execSQL("INSERT INTO di_account (moneysum, username, userid, name, type, icon, color, sequence, remark, createtime, updatetime, serverid, deletefalag) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					new Object[]{account.getMoneysum(),
							account.getUsername(),
							account.getUserid(),
							account.getName(),
							account.getType(),
							account.getIcon(),
							account.getColor(),
							account.getSequence(),
							account.getRemark(),
							account.getCreatetime(),
							account.getUpdatetime(),
							account.getId(),
							account.getDeletefalag()});
		}
	}
	
	/**
	 * 删除记录
	 * @param id 记录ID
	 */
	public void delete(Integer id){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("UPDATE di_account set deletefalag = 1 WHERE id = ?", new Object[]{id});
	}

	/**
	 * 更新记录
	 * @param account
	 */
	public void update(DiAccount account){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("UPDATE di_account SET moneysum=?, username=?, userid=?, name=?, type=?, icon=?, color=?, sequence=?, remark=?, createtime=?, updatetime=?, serverid=?, deletefalag = ? WHERE id=?",
				new Object[]{account.getMoneysum(), account.getUsername(),
						account.getUserid(), account.getName(),
						account.getType(), account.getIcon(), account.getColor(), account.getSequence(),
						account.getRemark(), account.getCreatetime(), account.getUpdatetime(),
						account.getServerid(), account.getDeletefalag(), account.getId()});
	}

	/**
	 * 查询记录
	 * @param id 记录ID
	 * @return
	 */
	public DiAccount find(Integer id){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, moneysum, username, userid, name, type, icon, color, sequence, remark, createtime, updatetime, serverid, deletefalag FROM di_account WHERE id=?", new String[]{id.toString()});
		if(cursor.moveToFirst()){
			return new DiAccount(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getDouble(cursor.getColumnIndex("moneysum")),
					cursor.getString(cursor.getColumnIndex("username")),
					cursor.getInt(cursor.getColumnIndex("userid")),
					cursor.getString(cursor.getColumnIndex("name")),
					cursor.getInt(cursor.getColumnIndex("type")),
					cursor.getString(cursor.getColumnIndex("icon")),
					cursor.getString(cursor.getColumnIndex("color")),
					cursor.getInt(cursor.getColumnIndex("sequence")),
					cursor.getString(cursor.getColumnIndex("remark")),
					cursor.getString(cursor.getColumnIndex("createtime")),
					cursor.getString(cursor.getColumnIndex("updatetime")),
					cursor.getInt(cursor.getColumnIndex("serverid")),
					cursor.getInt(cursor.getColumnIndex("deletefalag"))
			);
		}
		cursor.close();
		return null;
	}

	/**
	 * 查询记录
	 * @param name 记录ID
	 * @return
	 */
	public DiAccount find(String name){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, moneysum, username, userid, name, type, icon, color, sequence, remark, createtime, updatetime, serverid, deletefalag FROM di_account WHERE name=?", new String[]{name.toString()});
		if(cursor.moveToFirst()){
			return new DiAccount(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getDouble(cursor.getColumnIndex("moneysum")),
					cursor.getString(cursor.getColumnIndex("username")),
					cursor.getInt(cursor.getColumnIndex("userid")),
					cursor.getString(cursor.getColumnIndex("name")),
					cursor.getInt(cursor.getColumnIndex("type")),
					cursor.getString(cursor.getColumnIndex("icon")),
					cursor.getString(cursor.getColumnIndex("color")),
					cursor.getInt(cursor.getColumnIndex("sequence")),
					cursor.getString(cursor.getColumnIndex("remark")),
					cursor.getString(cursor.getColumnIndex("createtime")),
					cursor.getString(cursor.getColumnIndex("updatetime")),
					cursor.getInt(cursor.getColumnIndex("serverid")),
					cursor.getInt(cursor.getColumnIndex("deletefalag"))
			);
		}
		cursor.close();
		return null;
	}

	/**
	 * 查询记录
	 * @return
	 */
	public List<DiAccount> findAll(){
		List<DiAccount> accounts = new ArrayList<DiAccount>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, moneysum, username, userid, name, type, icon, color, sequence, remark, createtime, updatetime, serverid, deletefalag FROM di_account where deletefalag = 0;",
				null);
		while(cursor.moveToNext()){
			accounts.add(new DiAccount(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getDouble(cursor.getColumnIndex("moneysum")),
					cursor.getString(cursor.getColumnIndex("username")),
					cursor.getInt(cursor.getColumnIndex("userid")),
					cursor.getString(cursor.getColumnIndex("name")),
					cursor.getInt(cursor.getColumnIndex("type")),
					cursor.getString(cursor.getColumnIndex("icon")),
					cursor.getString(cursor.getColumnIndex("color")),
					cursor.getInt(cursor.getColumnIndex("sequence")),
					cursor.getString(cursor.getColumnIndex("remark")),
					cursor.getString(cursor.getColumnIndex("createtime")),
					cursor.getString(cursor.getColumnIndex("updatetime")),
					cursor.getInt(cursor.getColumnIndex("serverid")),
					cursor.getInt(cursor.getColumnIndex("deletefalag"))
			));
		}
		cursor.close();
		return accounts;

	}

}
