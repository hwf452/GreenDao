GreenDao使用总结

在Android开发过程中，有时候我们需要使用SQLite数据库去本地存储一些临时文件，之前，我们的做法是通过SQLiteOpenHelper实现创建数据库，
以及迭代开发中的数据库数据 内容 字段 变更时处理。


优点
1.通常我们在使用GreenDao的时候，我们只需定义数据模型，GreenDao框架将创建数据对象(实体)和DAO(数据访问对象)，能够节省部分代码。
2.不向性能妥协，使用了GreenDao，大多数实体可以以每秒几千个实体的速率进行插入，更新和加载。
3.GreenDao支持加密数据库来保护敏感数据。
4.微小的依赖库，GreenDao的关键依赖库大小不超过100kb.
5.如果需要，实体是可以被激活的。而活动实体可以透明的解析关系（我们要做的只是调用getter即可），并且有更新、删除和刷新方法，以便访问持久性功能。
6.GreenDao允许您将协议缓冲区(protobuf)对象直接保存到数据库中。如果您通过protobuf通话到您的服务器，则不需要另一个映射。
常规实体的所有持久性操作都可以用于protobuf对象。所以，相信这是GreenDao的独特之处。
7.自动生成代码，我们无需关注实体类以及Dao,因为GreenDao已经帮我们生成了。
8.开源 有兴趣的同学可以查看源码，深入去了解机制。


GreenDao对外提供的核心类简介

1.DaoMaster
DaoMaster保存数据库对象（SQLiteDatabase）并管理特定模式的Dao类。它具有静态方法来创建表或将他们删除。
其内部类OpenHelper和DevOpenHelper是在SQLite数据库中创建模式的SQLiteOpenHelper实现。

2.DaoSession
管理特定模式的所有可用Dao对象，您可以使用其中一个getter方法获取。DaoSession还为实体提供了一些通用的持久性方法，如插入，加载，更新，刷新和删除。最后，
DaoSession对象也跟踪一个身份范围。

3.Dao层
数据访问对象(Dao)持续存在并查询实体。对于每个实体，GreenDao生成一个Dao,它比DaoSession有更多的持久化方法，例如：count,loadAll和insertInTx。

4.实体
持久对象，通常实体是使用标准Java属性(如POJO或JavaBean)来表示数据库的对象。


GreenDao使用

1.在工程目录下build.gradle下添加插件

buildscript {
    
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.1.2'
        classpath 'org.greenrobot:greendao-gradle-plugin:3.2.2' // 添加插件 更好支持GreenDao

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
然后Sync Now即可。

2.在项目目录下的build.gradle下添加插件依赖

apply plugin: 'com.android.application'
apply plugin: 'org.greenrobot.greendao'

接下来继续添加依赖库
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'com.android.support:appcompat-v7:28.0.0-rc01'
    implementation 'com.android.support.constraint:constraint-layout:1.1.2'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
    implementation 'org.greenrobot:greendao:3.2.2' // 添加greendao库
}

greendao {
    schemaVersion 1
    daoPackage 'com.bjzc.greendao.dao' //换成自己的dao包名
    targetGenDir 'src/main/java'
}

然后Sync Now即可。


使用Demo
1.定义一个实体类

@Entity
public class PersonInfor {
    @Id(autoincrement = true)//设置自增长
    private Long id;

    @Index(unique = true)//设置唯一性
    private String perNo;//人员编号

    private String name;//人员姓名

    private String sex;//人员姓名
    }

2. Make Project
在菜单栏点 Build -> Make Project  编译一下工程，然后会自动生成GreenDao的 DaoMaster,DaoSession,UserDao三个依赖，同时Entity也会自动生成一些代码。

在我们Make Project后，实体内的变化如下所示：

@Entity
public class PersonInfor {
    @Id(autoincrement = true)//设置自增长
    private Long id;

    @Index(unique = true)//设置唯一性
    private String perNo;//人员编号

    private String name;//人员姓名

    private String sex;//人员姓名

    @Generated(hash = 1311768890)
    public PersonInfor(Long id, String perNo, String name, String sex) {
        this.id = id;
        this.perNo = perNo;
        this.name = name;
        this.sex = sex;
    }

    @Generated(hash = 1362534400)
    public PersonInfor() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPerNo() {
        return this.perNo;
    }

    public void setPerNo(String perNo) {
        this.perNo = perNo;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}


2.封装数据库操作类

因为我们对数据库的操作无非就是增删改查四个操作，所以我们将他们简单封装一下。

public class DbController {
    /**
     * Helper
     */
    private DaoMaster.DevOpenHelper mHelper;//获取Helper对象
    /**
     * 数据库
     */
    private SQLiteDatabase db;
    /**
     * DaoMaster
     */
    private DaoMaster mDaoMaster;
    /**
     * DaoSession
     */
    private DaoSession mDaoSession;
    /**
     * 上下文
     */
    private Context context;
    /**
     * dao
     */
    private PersonInforDao personInforDao;

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
        mHelper = new DaoMaster.DevOpenHelper(context,"person.db", null);
        mDaoMaster =new DaoMaster(getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        personInforDao = mDaoSession.getPersonInforDao();
    }
    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase(){
        if(mHelper == null){
            mHelper = new DaoMaster.DevOpenHelper(context,"person.db",null);
        }
        SQLiteDatabase db =mHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     * @return
     */
    private SQLiteDatabase getWritableDatabase(){
        if(mHelper == null){
            mHelper =new DaoMaster.DevOpenHelper(context,"person.db",null);
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db;
    }

    /**
     * 会自动判定是插入还是替换
     * @param personInfor
     */
    public void insertOrReplace(PersonInfor personInfor){
        personInforDao.insertOrReplace(personInfor);
    }
    /**插入一条记录，表里面要没有与之相同的记录
     *
     * @param personInfor
     */
    public long insert(PersonInfor personInfor){
      return  personInforDao.insert(personInfor);
    }

    /**
     * 更新数据
     * @param personInfor
     */
    public void update(PersonInfor personInfor){
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
        List<PersonInfor>personInfors = (List<PersonInfor>) personInforDao.queryBuilder().where(PersonInforDao.Properties.Name.eq(wherecluse)).build().unique();
        return personInfors;
    }
    /**
     * 查询所有数据
     */
    public List<PersonInfor> searchAll(){
        List<PersonInfor>personInfors=personInforDao.queryBuilder().list();
        return personInfors;
    }
    /**
     * 删除数据
     */
    public void delete(String wherecluse){
        personInforDao.queryBuilder().where(PersonInforDao.Properties.Name.eq(wherecluse)).buildDelete().executeDeleteWithoutDetachingEntities();
    }
}


3.GreenDao的使用

DbController mDbController = DbController.getInstance(MainActivity.this);
PersonInfor personInfor1 = new PersonInfor(null,"001","王大宝","男");

//Add
mDbController.insertOrReplace(personInfor1);

//Delete
mDbController.delete("王麻麻");

//Update
mDbController.update(personInfor1);
                
//Search
showDataList()


private void showDataList() {
        StringBuilder sb = new StringBuilder();
       List<PersonInfor>personInfors = mDbController.searchAll();
       for(PersonInfor personInfor:personInfors){
          // dataArea.setText("id:"+p);
           sb.append("id:").append(personInfor.getId())
             .append("perNo:").append(personInfor.getPerNo())
             .append("name:").append(personInfor.getName())
             .append("sex:").append(personInfor.getSex())
             .append("\n");
       }
       dataArea.setText(sb.toString());
    };
    
    
  代码说明：  
  
  细心的同学可以看到我们已经不用再使用创建数据库的sql语言了，因为GreenDao框架已经帮我们做了，
  通过之前的方式创建数据库过程当中还有可能出现一些莫名其妙的错误。
  我们只要定义出实体就可以了，从代码中可以看到我们创建了一个person.db的数据库。
  其实与我们平常操作就是这个Dao,这个框架会根据不同的数据实体对应生成不同的Dao,
  通过这个Dao我们就可以调用相应的接口去完成增删改查。
  
  
  
  
  DBManager 的用法:
  
  DBManager dbManager = DBManager.getInstance(this);
          for (int i = 0; i < 5; i++) {
              User user = new User();
              user.setId(i);
              user.setAge(i * 3);
              user.setName("第" + i + "人");
              dbManager.insertUser(user);
          }
          List<User> userList = dbManager.queryUserList();
          for (User user : userList) {
              Log.e("TAG", "queryUserList--before-->" + user.getId() + "--" + user.getName() +"--"+user.getAge());
              if (user.getId() == 0) {
                  dbManager.deleteUser(user);
              }
              if (user.getId() == 3) {
                  user.setAge(10);
                  dbManager.updateUser(user);
              }
          }
          userList = dbManager.queryUserList();
          for (User user : userList) {
              Log.e("TAG", "queryUserList--after--->" + user.getId() + "---" + user.getName()+"--"+user.getAge());
          }