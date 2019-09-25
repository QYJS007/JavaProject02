package work.protec.bus365.store.model;

import java.util.Date;

import utils.DateUtils;

public class ZenDaoTask{
	public String projectName; //项目名称
	public Integer taskNo; //任务编号
	public String taskName; //任务名称
	public Integer taskEstimate; //最初预计小时
	public Date taskDeadline; //截止时间
	public String taskState; //状态
	
	
	public Integer getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(Integer taskNo) {
		this.taskNo = taskNo;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Integer getTaskEstimate() {
		return taskEstimate;
	}

	public void setTaskEstimate(Integer taskEstimate) {
		this.taskEstimate = taskEstimate;
	}

	public Date getTaskDeadline() {
		return taskDeadline;
	}

	public void setTaskDeadline(Date taskDeadline) {
		this.taskDeadline = taskDeadline;
	}

	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}


	@Override
	public String toString() {
		return "ZenDaoTask [taskNo=" + taskNo + ", taskName=" + taskName
				+ ", taskEstimate=" + taskEstimate + ", taskDeadline="
				+ DateUtils.dateToStringYMd(taskDeadline) + ", taskState=" + taskState + "]";
	}
	
	
}
