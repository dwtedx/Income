package com.dwtedx.income.sqliteservice;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dwtedx.income.entity.DiType;
import com.dwtedx.income.provider.RecordFirstSharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class DITypeService {

	private static DITypeService mService = null;
	private DBOpenHelper dbOpenHelper;
	private Context mContext;

	private DITypeService(Context context) {
		this.mContext = context;
		this.dbOpenHelper = new DBOpenHelper(context);
	}

	public static DITypeService getInstance(Context context) {
		if (mService == null)
			mService = new DITypeService(context);
		return mService;
	}
	
	/**
	 * 添加记录
	 * @param type
	 */
	public void save(DiType type){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		//如果是第一次插入、添加一条时间轴开始节点
		RecordFirstSharedPreferences.init(mContext);
		db.execSQL("INSERT INTO di_type (username, userid, name, type, icon, color, sequence, remark, createtime, updatetime, serverid, deletefalag) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new Object[]{type.getUsername(),
						type.getUserid(),
						type.getName(),
						type.getType(),
						type.getIcon(),
						type.getColor(),
						type.getSequence(),
						type.getRemark(),
						type.getCreatetime(),
						type.getUpdatetime(),
						type.getServerid(),
						type.getDeletefalag()});
	}

	/**
	 * 同步
	 * @param type
	 */
	public void saveSynchronizeEgain(DiType type){
		DiType diType1 = this.find(type.getName());
		if(null == diType1) {
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			//如果是第一次插入、添加一条时间轴开始节点
			RecordFirstSharedPreferences.init(mContext);
			db.execSQL("INSERT INTO di_type (username, userid, name, type, icon, color, sequence, remark, createtime, updatetime, serverid, deletefalag) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					new Object[]{type.getUsername(),
							type.getUserid(),
							type.getName(),
							type.getType(),
							type.getIcon(),
							type.getColor(),
							type.getSequence(),
							type.getRemark(),
							type.getCreatetime(),
							type.getUpdatetime(),
							type.getId(),
							type.getDeletefalag()});
		}
	}
	
	/**
	 * 删除记录
	 * @param id 记录ID
	 */
	public void delete(Integer id){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("UPDATE di_type set deletefalag = 1 WHERE id = ?", new Object[]{id});
	}

	/**
	 * 更新记录
	 * @param type
	 */
	public void update(DiType type){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("UPDATE di_type SET username=?, userid=?, name=?, type=?, icon=?, color=?, sequence=?, remark=?, createtime=?, updatetime=?, serverid=?, deletefalag=? WHERE id=?",
				new Object[]{type.getUsername(),
						type.getUserid(), type.getName(),
						type.getType(), type.getIcon(), type.getColor(), type.getSequence(),
						type.getRemark(), type.getCreatetime(),
						type.getUpdatetime(), type.getServerid(), type.getDeletefalag(), type.getId()});
	}

	/**
	 * 查询记录
	 * @param id 记录ID
	 * @return
	 */
	public DiType find(Integer id){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, username, userid, name, type, icon, color, sequence, remark, createtime, updatetime, serverid, deletefalag FROM di_type WHERE id=?", new String[]{id.toString()});
		if(cursor.moveToFirst()){
			return new DiType(
					cursor.getInt(cursor.getColumnIndex("id")),
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
	public DiType find(String name){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, username, userid, name, type, icon, color, sequence, remark, createtime, updatetime, serverid, deletefalag FROM di_type WHERE name=?", new String[]{name.toString()});
		if(cursor.moveToFirst()){
			return new DiType(
					cursor.getInt(cursor.getColumnIndex("id")),
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
	public List<DiType> findAll(Integer type){
		List<DiType> types = new ArrayList<DiType>();
		if(0 == type){
			types = findAll();
			return types;
		}
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, username, userid, name, type, icon, color, sequence, remark, createtime, updatetime, serverid, deletefalag FROM di_type where deletefalag = 0 and type = ? order by sequence asc;",
				new String[]{type.toString()});
		while(cursor.moveToNext()){
			types.add(new DiType(
					cursor.getInt(cursor.getColumnIndex("id")),
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
		return types;

	}

	/**
	 * 查询记录
	 * @return
	 */
	public List<DiType> findAll(){
		List<DiType> types = new ArrayList<DiType>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, username, userid, name, type, icon, color, sequence, remark, createtime, updatetime, serverid, deletefalag FROM di_type where deletefalag = 0 order by type desc;", null);
		while(cursor.moveToNext()){
			types.add(new DiType(
					cursor.getInt(cursor.getColumnIndex("id")),
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
		return types;

	}

}
