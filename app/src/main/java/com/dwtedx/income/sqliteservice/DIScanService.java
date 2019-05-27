package com.dwtedx.income.sqliteservice;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dwtedx.income.entity.DiScan;
import com.dwtedx.income.provider.RecordFirstSharedPreferences;
import com.dwtedx.income.utility.CommonConstants;

import java.util.ArrayList;
import java.util.List;

public class DIScanService {

	private static DIScanService mService = null;
	private DBOpenHelper dbOpenHelper;
	private Context mContext;

	private DIScanService(Context context) {
		this.mContext = context;
		this.dbOpenHelper = new DBOpenHelper(context);
	}

	public static DIScanService getInstance(Context context) {
		if (mService == null)
			mService = new DIScanService(context);
		return mService;
	}
	
	/**
	 * 添加记录
	 * @param diScan
	 */
	public void save(DiScan diScan){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		//如果是第一次插入、添加一条时间轴开始节点
		RecordFirstSharedPreferences.init(mContext);
		db.execSQL("INSERT INTO di_scan (username, userid, incomeid, moneysum, name, store, brand, quantity, type, sequence, remark, createtime, updatetime, serverid, deletefalag, isupdate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new Object[]{diScan.getUsername(),
						diScan.getUserid(),
						diScan.getIncomeid(),
						diScan.getMoneysum(),
						diScan.getName(),
						diScan.getStore(),
						diScan.getBrand(),
						diScan.getQuantity(),
						diScan.getType(),
						diScan.getSequence(),
						diScan.getRemark(),
						diScan.getCreatetime(),
						diScan.getUpdatetime(),
						diScan.getServerid(),
						diScan.getDeletefalag(),
						diScan.getIsupdate()});
	}

	/**
	 * 同步
	 * @param diScan
	 */
	public void saveSynchronizeEgain(DiScan diScan){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		//如果是第一次插入、添加一条时间轴开始节点
		RecordFirstSharedPreferences.init(mContext);
		db.execSQL("INSERT INTO di_scan (username, userid, incomeid, moneysum, name, store, brand, quantity, type, sequence, remark, createtime, updatetime, serverid, deletefalag, isupdate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new Object[]{diScan.getUsername(),
						diScan.getUserid(),
						diScan.getIncomeid(),
						diScan.getMoneysum(),
						diScan.getName(),
						diScan.getStore(),
						diScan.getBrand(),
						diScan.getQuantity(),
						diScan.getType(),
						diScan.getSequence(),
						diScan.getRemark(),
						diScan.getCreatetime(),
						diScan.getUpdatetime(),
						diScan.getId(),
						diScan.getDeletefalag(),
						diScan.getIsupdate()});

	}
	
	/**
	 * 删除记录
	 * @param id 记录ID
	 */
	public void delete(Integer id){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("UPDATE di_scan set deletefalag = 1 WHERE id = ?", new Object[]{id});
	}

	/**
	 * 删除记录
	 */
	public void deleteAll(){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("DELETE FROM di_scan WHERE 1 = 1");
		db.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE name = ?", new Object[]{"di_scan"});
	}

	/**
	 * 更新记录
	 * @param scan
	 */
	public void update(DiScan scan){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("UPDATE di_scan SET username=?, userid=?, incomeid=?, moneysum=?, name=?, store=?, brand=?, quantity=?, type=?, sequence=?, remark=?, createtime=?, updatetime=?, serverid=?, deletefalag=?, isupdate=? WHERE id=?",
				new Object[]{scan.getUsername(), scan.getUserid(), scan.getIncomeid(), scan.getMoneysum(),
						scan.getName(), scan.getStore(), scan.getBrand(), scan.getQuantity(),
						scan.getType(), scan.getSequence(), scan.getRemark(), scan.getCreatetime(),
						scan.getUpdatetime(), scan.getServerid(), scan.getDeletefalag(), scan.getIsupdate(), scan.getId()});
	}

	/**
	 * 根据收入id删除扫单
	 * @param incomeId
	 */
	public void deleteByIncomeId(int incomeId){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("UPDATE di_scan SET deletefalag=? WHERE incomeid=?",
				new Object[]{CommonConstants.DELETEFALAG_DELETEED, incomeId});
	}

	/**
	 * 查询记录
	 * @param id 记录ID
	 * @return
	 */
	public DiScan find(Integer id){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, username, userid, incomeid, moneysum, name, store, brand, quantity, type, sequence, remark, createtime, updatetime, serverid, deletefalag, isupdate FROM di_scan WHERE id=?", new String[]{id.toString()});
		if(cursor.moveToFirst()){
			return new DiScan(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getString(cursor.getColumnIndex("username")),
					cursor.getInt(cursor.getColumnIndex("userid")),
					cursor.getInt(cursor.getColumnIndex("incomeid")),
					cursor.getDouble(cursor.getColumnIndex("moneysum")),
					cursor.getString(cursor.getColumnIndex("name")),
					cursor.getString(cursor.getColumnIndex("store")),
					cursor.getString(cursor.getColumnIndex("brand")),
					cursor.getInt(cursor.getColumnIndex("quantity")),
					cursor.getInt(cursor.getColumnIndex("type")),
					cursor.getInt(cursor.getColumnIndex("sequence")),
					cursor.getString(cursor.getColumnIndex("remark")),
					cursor.getString(cursor.getColumnIndex("createtime")),
					cursor.getString(cursor.getColumnIndex("updatetime")),
					cursor.getInt(cursor.getColumnIndex("serverid")),
					cursor.getInt(cursor.getColumnIndex("deletefalag")),
					cursor.getInt(cursor.getColumnIndex("isupdate"))
			);
		}
		cursor.close();
		return null;
	}

	/**
	 * 查询记录
	 * @return
	 */
	public List<DiScan> findByIncomeId(Integer incomeId){
		List<DiScan> scanList = new ArrayList<DiScan>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, username, userid, incomeid, moneysum, name, store, brand, quantity, type, sequence, remark, createtime, updatetime, serverid, deletefalag, isupdate FROM di_scan where deletefalag = 0 and incomeid = ? order by sequence asc;",
				new String[]{incomeId.toString()});
		while(cursor.moveToNext()){
			scanList.add(new DiScan(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getString(cursor.getColumnIndex("username")),
					cursor.getInt(cursor.getColumnIndex("userid")),
					cursor.getInt(cursor.getColumnIndex("incomeid")),
					cursor.getDouble(cursor.getColumnIndex("moneysum")),
					cursor.getString(cursor.getColumnIndex("name")),
					cursor.getString(cursor.getColumnIndex("store")),
					cursor.getString(cursor.getColumnIndex("brand")),
					cursor.getInt(cursor.getColumnIndex("quantity")),
					cursor.getInt(cursor.getColumnIndex("type")),
					cursor.getInt(cursor.getColumnIndex("sequence")),
					cursor.getString(cursor.getColumnIndex("remark")),
					cursor.getString(cursor.getColumnIndex("createtime")),
					cursor.getString(cursor.getColumnIndex("updatetime")),
					cursor.getInt(cursor.getColumnIndex("serverid")),
					cursor.getInt(cursor.getColumnIndex("deletefalag")),
					cursor.getInt(cursor.getColumnIndex("isupdate"))
			));
		}
		cursor.close();
		return scanList;
	}

}
