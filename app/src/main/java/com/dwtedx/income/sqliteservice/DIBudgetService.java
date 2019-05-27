package com.dwtedx.income.sqliteservice;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dwtedx.income.entity.DiBudget;
import com.dwtedx.income.provider.BudgetSharedPreferences;
import com.dwtedx.income.provider.RecordFirstSharedPreferences;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DIBudgetService {

	private static DIBudgetService mService = null;
	private DBOpenHelper dbOpenHelper;
	private Context mContext;

	private DIBudgetService(Context context) {
		this.mContext = context;
		this.dbOpenHelper = new DBOpenHelper(context);
	}

	public static DIBudgetService getInstance(Context context) {
		if (mService == null)
			mService = new DIBudgetService(context);
		return mService;
	}
	
	/**
	 * 添加记录
	 * @param budget
	 */
	public void save(DiBudget budget){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		//如果是第一次插入、添加一条时间轴开始节点
		RecordFirstSharedPreferences.init(mContext);
		db.execSQL("INSERT INTO di_budget (moneysum, moneylast, username, userid, yearnom, monthnom, remark, createtime, updatetime, isupdate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
				new Object[]{budget.getMoneysum(),
						budget.getMoneylast(),
						budget.getUsername(),
						budget.getUserid(),
						budget.getYearnom(),
						budget.getMonthnom(),
						budget.getRemark(),
						budget.getCreatetime(),
						budget.getUpdatetime(),
						budget.getIsupdate()});
	}
	
	/**
	 * 删除记录
	 * @param id 记录ID
	 */
	public void delete(Integer id){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("DELETE FROM di_account WHERE id = ?", new Object[]{id});
	}

	/**
	 * 更新记录
	 * @param budget
	 */
	public void update(DiBudget budget){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("UPDATE di_budget SET moneysum=?, moneylast=?, username=?, userid=?, yearnom=?, monthnom=?, remark=?, createtime=?, updatetime=?, isupdate=? WHERE id=?",
				new Object[]{budget.getMoneysum(),
						budget.getMoneylast(),
						budget.getUsername(),
						budget.getUserid(),
						budget.getYearnom(),
						budget.getMonthnom(),
						budget.getRemark(),
						budget.getCreatetime(),
						budget.getUpdatetime(),
						budget.getIsupdate(), budget.getId()});
	}

	/**
	 * 查询记录
	 * @param yearnom 记录ID
	 * @param monthnom 记录ID
	 * @return
	 */
	public DiBudget findByYearMonth(Integer yearnom, Integer monthnom){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, moneysum, moneylast, username, userid, yearnom, monthnom, remark, createtime, updatetime, isupdate FROM di_budget WHERE yearnom=? and monthnom=?", new String[]{yearnom.toString(), monthnom.toString()});
		if(cursor.moveToFirst()){
			return new DiBudget(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getDouble(cursor.getColumnIndex("moneysum")),
					cursor.getDouble(cursor.getColumnIndex("moneylast")),
					cursor.getString(cursor.getColumnIndex("username")),
					cursor.getInt(cursor.getColumnIndex("userid")),
					cursor.getInt(cursor.getColumnIndex("yearnom")),
					cursor.getInt(cursor.getColumnIndex("monthnom")),
					cursor.getString(cursor.getColumnIndex("remark")),
					cursor.getString(cursor.getColumnIndex("createtime")),
					cursor.getString(cursor.getColumnIndex("updatetime")),
					cursor.getInt(cursor.getColumnIndex("isupdate"))
			);
		}else{
			BudgetSharedPreferences.init(mContext);
			DiBudget budget = new DiBudget();
			budget.setMoneysum(BudgetSharedPreferences.getBudget());
			budget.setMoneylast(BudgetSharedPreferences.getBudget());
			budget.setMonthnom(monthnom);
			budget.setYearnom(yearnom);
			budget.setUpdatetime(CommonUtility.getCurrentTime());
			budget.setCreatetime(CommonUtility.getCurrentTime());
			budget.setIsupdate(CommonConstants.INCOME_RECORD_NOT_UPDATE);
			save(budget);
			Cursor cursor1 = db.rawQuery("SELECT id, moneysum, moneylast, username, userid, yearnom, monthnom, remark, createtime, updatetime, isupdate FROM di_budget WHERE yearnom=? and monthnom=?", new String[]{String.valueOf(yearnom), String.valueOf(monthnom)});
			if(cursor1.moveToFirst()) {
				return new DiBudget(
						cursor1.getInt(cursor.getColumnIndex("id")),
						cursor1.getDouble(cursor.getColumnIndex("moneysum")),
						cursor1.getDouble(cursor.getColumnIndex("moneylast")),
						cursor1.getString(cursor.getColumnIndex("username")),
						cursor1.getInt(cursor.getColumnIndex("userid")),
						cursor1.getInt(cursor.getColumnIndex("yearnom")),
						cursor1.getInt(cursor.getColumnIndex("monthnom")),
						cursor1.getString(cursor.getColumnIndex("remark")),
						cursor1.getString(cursor.getColumnIndex("createtime")),
						cursor1.getString(cursor.getColumnIndex("updatetime")),
						cursor1.getInt(cursor.getColumnIndex("isupdate")));
			}
		}
		cursor.close();
		return null;
	}

	/**
	 * 查询最新记录行
	 * @return
	 */
	public DiBudget findLastRow(){
		Calendar ca = Calendar.getInstance();
		String yearStr = String.valueOf(ca.get(Calendar.YEAR));
		String monthStr = String.valueOf((ca.get(Calendar.MONTH) + 1));
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, moneysum, moneylast, username, userid, yearnom, monthnom, remark, createtime, updatetime, isupdate FROM di_budget WHERE yearnom=? and monthnom=?", new String[]{yearStr, monthStr});
		if(cursor.moveToFirst()){
			DiBudget budget = new DiBudget(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getDouble(cursor.getColumnIndex("moneysum")),
					cursor.getDouble(cursor.getColumnIndex("moneylast")),
					cursor.getString(cursor.getColumnIndex("username")),
					cursor.getInt(cursor.getColumnIndex("userid")),
					cursor.getInt(cursor.getColumnIndex("yearnom")),
					cursor.getInt(cursor.getColumnIndex("monthnom")),
					cursor.getString(cursor.getColumnIndex("remark")),
					cursor.getString(cursor.getColumnIndex("createtime")),
					cursor.getString(cursor.getColumnIndex("updatetime")),
					cursor.getInt(cursor.getColumnIndex("isupdate"))
			);
			return budget;
		}else{
			BudgetSharedPreferences.init(mContext);
			DiBudget budget = new DiBudget();
			budget.setMoneysum(BudgetSharedPreferences.getBudget());
			budget.setMoneylast(BudgetSharedPreferences.getBudget());
			budget.setMonthnom((ca.get(Calendar.MONTH) + 1));
			budget.setYearnom((ca.get(Calendar.YEAR)));
			budget.setUpdatetime(CommonUtility.getCurrentTime());
			budget.setCreatetime(CommonUtility.getCurrentTime());
			budget.setIsupdate(CommonConstants.INCOME_RECORD_NOT_UPDATE);
			save(budget);
			Cursor cursor1 = db.rawQuery("SELECT id, moneysum, moneylast, username, userid, yearnom, monthnom, remark, createtime, updatetime, isupdate FROM di_budget WHERE yearnom=? and monthnom=?", new String[]{yearStr, monthStr});
			if(cursor1.moveToFirst()) {
				return new DiBudget(
						cursor1.getInt(cursor.getColumnIndex("id")),
						cursor1.getDouble(cursor.getColumnIndex("moneysum")),
						cursor1.getDouble(cursor.getColumnIndex("moneylast")),
						cursor1.getString(cursor.getColumnIndex("username")),
						cursor1.getInt(cursor.getColumnIndex("userid")),
						cursor1.getInt(cursor.getColumnIndex("yearnom")),
						cursor1.getInt(cursor.getColumnIndex("monthnom")),
						cursor1.getString(cursor.getColumnIndex("remark")),
						cursor1.getString(cursor.getColumnIndex("createtime")),
						cursor1.getString(cursor.getColumnIndex("updatetime")),
						cursor1.getInt(cursor.getColumnIndex("isupdate")));
			}
		}
		cursor.close();
		return null;
	}

	/**
	 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/9/16 10:21.
	 * Company 路之遥网络科技有限公司
	 * Description 查询没有同步的记录
	 */
	public List<DiBudget> findNotUpdate(int year, int month){
		List<DiBudget> incomes = new ArrayList<DiBudget>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, moneysum, moneylast, username, userid, yearnom, monthnom, remark, createtime, updatetime, isupdate FROM di_budget WHERE isupdate=" + CommonConstants.INCOME_RECORD_NOT_UPDATE + "", null);

		while(cursor.moveToNext()){
			int yeardb = cursor.getInt(cursor.getColumnIndex("yearnom"));
			int monthdb = cursor.getInt(cursor.getColumnIndex("monthnom"));
			if(year == yeardb && month == monthdb){
				continue;
			}
			incomes.add(new DiBudget(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getDouble(cursor.getColumnIndex("moneysum")),
					cursor.getDouble(cursor.getColumnIndex("moneylast")),
					cursor.getString(cursor.getColumnIndex("username")),
					cursor.getInt(cursor.getColumnIndex("userid")),
					yeardb, monthdb,
					cursor.getString(cursor.getColumnIndex("remark")),
					cursor.getString(cursor.getColumnIndex("createtime")),
					cursor.getString(cursor.getColumnIndex("updatetime")),
					cursor.getInt(cursor.getColumnIndex("isupdate"))
			));
		}
		cursor.close();
		return incomes;
	}

	/**
	 * 查询记录
	 * @return
	 */
	public List<DiBudget> findAll(){
		List<DiBudget> budgets = new ArrayList<DiBudget>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, moneysum, moneylast, username, userid, yearnom, monthnom, remark, createtime, updatetime, isupdate FROM di_budget order by createtime desc;",
				null);
		while(cursor.moveToNext()){
			budgets.add(new DiBudget(
					cursor.getInt(cursor.getColumnIndex("id")),
					cursor.getDouble(cursor.getColumnIndex("moneysum")),
					cursor.getDouble(cursor.getColumnIndex("moneylast")),
					cursor.getString(cursor.getColumnIndex("username")),
					cursor.getInt(cursor.getColumnIndex("userid")),
					cursor.getInt(cursor.getColumnIndex("yearnom")),
					cursor.getInt(cursor.getColumnIndex("monthnom")),
					cursor.getString(cursor.getColumnIndex("remark")),
					cursor.getString(cursor.getColumnIndex("createtime")),
					cursor.getString(cursor.getColumnIndex("updatetime")),
					cursor.getInt(cursor.getColumnIndex("isupdate"))
			));
		}
		cursor.close();
		return budgets;

	}

}
