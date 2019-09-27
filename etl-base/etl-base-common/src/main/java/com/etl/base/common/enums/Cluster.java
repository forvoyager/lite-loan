package com.etl.base.common.enums;

/**
 * 集群节点属性
 */
public enum Cluster {

  master("主节点"),
  slave("从节点"),
  ;

  private String label;

  private Cluster(String label){
    this.label = label;
  }
}
