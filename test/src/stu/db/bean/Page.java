package stu.db.bean;

import java.util.List;
public class Page
{
  private Integer totalNum;
  private List list;

  public Integer getTotalNum()
  {
    return this.totalNum;
  }

  public void setTotalNum(Integer totalNum) {
    this.totalNum = totalNum;
  }

  public List getList() {
    return this.list;
  }

  public void setList(List list) {
    this.list = list;
  }
}

