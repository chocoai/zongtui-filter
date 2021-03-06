package com.zongtui.filter.simhash;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;

/**
 * 将数据存储到BerkeleyDB数据库中
 * 
 * ClassName: BerkeleyDataStore <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: 2015年4月13日 下午11:47:02 <br/>
 *
 * @author Administrator
 * @version
 * @since JDK 1.7
 */
public class BerkeleyDataStore implements KeyValueDataStore<String, String> {
	/**
	 * 日志记录器
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 数据库实例对象
	 */
	private Database myDatabase = null;
	/**
	 * 数据库的环境信息
	 */
	private Environment myDbEnvironment;

	public BerkeleyDataStore() {
	}

	public void init(String dbName) {
		logger.info("打开数据库: " + dbName);
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setAllowCreate(true);
		envConfig.setTransactional(true);
		envConfig.setReadOnly(false);
		envConfig.setTxnTimeout(10000, TimeUnit.MILLISECONDS);
		envConfig.setLockTimeout(10000, TimeUnit.MILLISECONDS);
		File file = new File(dbName);
		if (!file.exists())
			file.mkdirs();
		myDbEnvironment = new Environment(file, envConfig);
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		dbConfig.setTransactional(true);
		dbConfig.setReadOnly(false);
		myDatabase = myDbEnvironment.openDatabase(null, dbName, dbConfig);
	}

	@Override
	public void putToStore(String dbName, String key, String value) {
		DatabaseEntry theKey = null;
		Transaction txn = null;
		txn = this.myDbEnvironment.beginTransaction(null, null);
		theKey = new DatabaseEntry(Strings.nullToEmpty(key).getBytes(
				StandardCharsets.UTF_8));
		DatabaseEntry theData = new DatabaseEntry(Strings.nullToEmpty(value)
				.getBytes(StandardCharsets.UTF_8));
		OperationStatus res = myDatabase.put(txn, theKey, theData);
		if (res == OperationStatus.SUCCESS) {
			logger.info("向数据库 {} 中写入:{},{}", dbName, key, value);
		} else if (res == OperationStatus.KEYEXIST) {
			logger.info("向数据库 {} 中写入:{},{}失败,该值已经存在", dbName, key, value);
		} else {
			logger.info("向数据库 {} 中写入:{},{}失败", dbName, key, value);
		}
		txn.commit();
		if (txn != null) {
			txn.abort();
		}
	}

	public Environment getDbEnvironment() {
		return myDbEnvironment;
	}

	@Override
	public String getFromStore(String dbName, String key) {
		String result = null;
		Transaction txn = null;
		try {
			txn = this.myDbEnvironment.beginTransaction(null, null);
			DatabaseEntry theKey = new DatabaseEntry(
					key.getBytes(StandardCharsets.UTF_8));
			DatabaseEntry theData = new DatabaseEntry();
			OperationStatus opStatus = myDatabase.get(null, theKey, theData,
					LockMode.DEFAULT);
			if (opStatus == OperationStatus.SUCCESS) {
				result = new String(theData.getData(), StandardCharsets.UTF_8);
				logger.info("从数据库 {} 中读取:{},{}", dbName, key, result);
			} else {
				logger.info("No record found for key {}", key);
			}
			txn.commit();
		} catch (Exception e) {
			if (txn != null) {
				txn.abort();
			}
			logger.error("从数据库{}中读取:{},出现lock异常", new String[] { dbName, key },
					e);
		}
		return result;
	}

	public Database getMyDatabase() {
		return myDatabase;
	}

	@Override
	public void deleteFromStore(String dbName, String key) {
		Transaction txn = null;
		try {
			txn = this.myDbEnvironment.beginTransaction(null, null);
			DatabaseEntry theKey = new DatabaseEntry(
					key.getBytes(StandardCharsets.UTF_8));
			OperationStatus res = myDatabase.delete(null, theKey);
			if (res == OperationStatus.SUCCESS) {
				logger.info("从数据库{}中删除:{}", dbName, key);
			} else if (res == OperationStatus.KEYEMPTY) {
				logger.info("没有从数据库{}中找到:{}。无法删除", dbName, key);
			} else {
				logger.info("删除操作失败，由于{}", res.toString());
			}
			txn.commit();
		} catch (Exception e) {
			if (txn != null) {
				txn.abort();
			}
			logger.error("从数据库中 {} 删除数据 {} 失败，出现错误",
					new String[] { dbName, key }, e);
		}
	}

	public void closeConnection() {
		try {
			if (myDatabase != null) {
				myDatabase.close();
			}
			if (myDbEnvironment != null) {
				myDbEnvironment.cleanLog();
				myDbEnvironment.close();
			}
		} catch (DatabaseException dbe) {
			logger.error("不能关闭数据库信息", dbe);
		}
	}
}
