package com.loris.base.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.Log;
import com.loris.base.bean.UploadRecord;
import com.loris.base.bean.User;
import com.loris.base.repository.service.LogService;
import com.loris.base.repository.service.UploadRecordService;
import com.loris.base.repository.service.UserService;
import com.loris.base.web.config.setting.DownSetting;
import com.loris.base.repository.service.DownSettingService;

@Component
public class BasicManager
{
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected LogService logService;
	
	@Autowired
	protected DownSettingService downSettingService;
	
	@Autowired
	protected UploadRecordService uploadRecordService;
	
	/**
	 * 获得下载页面
	 * @param page 页面配置参数
	 * @param ew 过滤条件
	 * @return 页数据
	 */
	public Page<DownSetting> getDownSettingPage(Page<DownSetting> page, EntityWrapper<DownSetting> ew)
	{
		Page<DownSetting> list = downSettingService.selectPage(page, ew);
		List<DownSetting> records = list.getRecords();
		for (DownSetting downSetting : records)
		{
			downSetting.decodeParams();
		}
		return list;
	}
	
	/**
	 * 获得数据配置参数
	 * @param id ID值
	 * @return 配置数据
	 */
	public DownSetting selectById(String id)
	{
		DownSetting setting = downSettingService.selectById(id);
		if(setting != null)
		{
			setting.decodeParams();
		}
		return setting;
	}
	
	/**
	 * 更新数据配置到数据库中
	 * @param setting 数据设置
	 * @return 是否成功的标志
	 */
	public boolean addOrUpdateDownSettingById(DownSetting setting)
	{
		if(setting != null)
		{
			setting.encodeParams();
		}
		if(StringUtils.isNotEmpty(setting.getId()))
		{
			return downSettingService.updateById(setting);
		}
		else
		{
			setting.create();
			return downSettingService.insert(setting);
		}
	}
	
	/**
	 * 保存数据到数据库中
	 * @param setting
	 * @return
	 */
	public boolean addDownSetting(DownSetting setting)
	{
		return downSettingService.insert(setting);
	}
	
	/**
	 * 获得下载的数据记录
	 * @param ew 过滤条件
	 * @return 下载列表
	 */
	public List<DownSetting> getDownSettings(EntityWrapper<DownSetting> ew)
	{
		return downSettingService.selectList(ew);
	}
	
	/**
	 * 通过用户名称获得用户
	 * @param name 名称
	 * @return 用户
	 */
	public User getUser(String name)
	{
		EntityWrapper<User> ew = new EntityWrapper<>();
		ew.eq("username", name);
		return userService.selectOne(ew);
	}
	
	/**
	 * 注册用户或更新用户信息
	 * @param user
	 * @return
	 */
	public boolean addOrUpdateUser(User user)
	{
		if(StringUtils.isEmpty(user.getId()))
		{
			user.create();
			return userService.insert(user);
		}
		else
		{
			return userService.updateById(user);
		}
	}
	
	/**
	 * 通过用户ID值称获得用户信息
	 * @param userid 用户ID值
	 * @return 用户
	 */
	public User getUserById(String userid)
	{
		return userService.selectById(userid);
	}
	
	/**
	 * 添加日志记录
	 * @param log 日志实体
	 * @return 是否添加成功
	 */
	public boolean addLog(Log log)
	{
		return logService.insert(log);
	}
	
	/**
	 * 用户列表
	 * @return 用户列表
	 */
	public List<User> getUserList()
	{
		EntityWrapper<User> ew = new EntityWrapper<>();
		return userService.selectList(ew);
	}
	
	/**
	 * 记录数据更新的记录
	 * @param rec
	 * @return
	 */
	public boolean addUploadRecrod(UploadRecord rec)
	{
		return uploadRecordService.insert(rec);
	}
	
	/**
	 * 获得数据的记录
	 * @param type
	 * @param start
	 * @param end
	 * @return
	 */
	public List<UploadRecord> getUploadRecord(String type, String start, String end)
	{
		EntityWrapper<UploadRecord> ew = new EntityWrapper<>();
		ew.eq("type", type);
		if(StringUtils.isNotEmpty(start))
		{
			ew.and().gt("updatetime", start);
		}
		if(StringUtils.isNotEmpty(end))
		{
			ew.and().lt("updatetime", end);
		}
		return uploadRecordService.selectList(ew);
	}
	
	/**
	 * 获得数据的记录
	 * @param type
	 * @param start
	 * @param end
	 * @return
	 */
	public List<UploadRecord> getUploadRecordById(String dataid, String start, String end)
	{
		EntityWrapper<UploadRecord> ew = new EntityWrapper<>();
		ew.eq("dataid", dataid);
		if(StringUtils.isNotEmpty(start))
		{
			ew.and().gt("updatetime", start);
		}
		if(StringUtils.isNotEmpty(end))
		{
			ew.and().lt("updatetime", end);
		}
		return uploadRecordService.selectList(ew);
	}
}
