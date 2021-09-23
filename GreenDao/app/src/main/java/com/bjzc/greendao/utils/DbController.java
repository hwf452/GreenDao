package com.bjzc.greendao.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.bjzc.greendao.dao.DaoMaster;
import com.bjzc.greendao.dao.DaoSession;
import com.bjzc.greendao.dao.PersonInforDao;
import com.bjzc.greendao.entity.PersonInfor;

import java.util.List;

public class DbController {
    /**
     * 数据库名称
     */
    private final static String dbName = "DbController.db";
    /**
     * Helper
     */
    private DaoMaster.DevOpenHelper mHelper;//获取Helper对象
    /**
     * 数据库
     */
    private SQLiteDatabase db;
    /**
     * 上下文
     */
    private Context context;
    private static DbController mDbController;
    /**
     * 获取单例
     */
    public static DbController getInstance(Context context){
        if(mDbController == null){
            synchronized (DbController.class){
                if(mDbController == null){
                    mDbController = new DbController(context);
                }
            }
        }
        return mDbController;
    }
    /**
     * 初始化
     * @param context
     */
    public DbController(Context context) {
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(context,dbName, null);
    }
    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase(){
        if(mHelper == null){
            mHelper = new DaoMaster.DevOpenHelper(context,dbName,null);
        }
        return mHelper.getReadableDatabase();
    }

    /**
     * 获取可写数据库
     * @return
     */
    private SQLiteDatabase getWritableDatabase(){
        if(mHelper == null){
            mHelper =new DaoMaster.DevOpenHelper(context,dbName,null);
        }
        return mHelper.getWritableDatabase();
    }

    /**
     * 会自动判定是插入还是替换
     * @param personInfor
     */
    public void insertOrReplace(PersonInfor personInfor){
        DaoMaster mDaoMaster =new DaoMaster(getWritableDatabase());
        DaoSession mDaoSession = mDaoMaster.newSession();
        PersonInforDao personInforDao = mDaoSession.getPersonInforDao();
        personInforDao.insertOrReplace(personInfor);
    }
    /**插入一条记录，表里面要没有与之相同的记录
     *
     * @param personInfor
     */
    public long insert(PersonInfor personInfor){
        DaoMaster mDaoMaster =new DaoMaster(getWritableDatabase());
        DaoSession mDaoSession = mDaoMaster.newSession();
        PersonInforDao personInforDao = mDaoSession.getPersonInforDao();
        return  personInforDao.insert(personInfor);
    }

    /**
     * 更新数据
     * @param personInfor
     */
    public void update(PersonInfor personInfor){
        DaoMaster mDaoMaster =new DaoMaster(getWritableDatabase());
        DaoSession mDaoSession = mDaoMaster.newSession();
        PersonInforDao personInforDao = mDaoSession.getPersonInforDao();
        PersonInfor mOldPersonInfor = personInforDao.queryBuilder().where(PersonInforDao.Properties.Id.eq(personInfor.getId())).build().unique();//拿到之前的记录
        if(mOldPersonInfor !=null){
            mOldPersonInfor.setName("张三");
            personInforDao.update(mOldPersonInfor);
        }
    }
    /**
     * 按条件查询数据
     */
    public List<PersonInfor> searchByWhere(String wherecluse){
        DaoMaster mDaoMaster =new DaoMaster(getReadableDatabase());
        DaoSession mDaoSession = mDaoMaster.newSession();
        PersonInforDao personInforDao = mDaoSession.getPersonInforDao();
        List<PersonInfor>personInfors = (List<PersonInfor>) personInforDao.queryBuilder().where(PersonInforDao.Properties.Name.eq(wherecluse)).build().unique();
        return personInfors;
    }
    /**
     * 查询所有数据
     */
    public List<PersonInfor> searchAll(){
        DaoMaster mDaoMaster =new DaoMaster(getReadableDatabase());
        DaoSession mDaoSession = mDaoMaster.newSession();
        PersonInforDao personInforDao = mDaoSession.getPersonInforDao();
        return personInforDao.queryBuilder().list();
    }
    /**
     * 删除数据
     */
    public void delete(String wherecluse){
        DaoMaster mDaoMaster =new DaoMaster(getWritableDatabase());
        DaoSession mDaoSession = mDaoMaster.newSession();
        PersonInforDao personInforDao = mDaoSession.getPersonInforDao();
        personInforDao.queryBuilder().where(PersonInforDao.Properties.Name.eq(wherecluse)).buildDelete().executeDeleteWithoutDetachingEntities();
    }
}