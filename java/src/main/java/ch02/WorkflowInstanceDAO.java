package ch02;


/** Data Access Object for workflow instances */
public class WorkflowInstanceDAO {

  Db db;
  WorkflowDAO workflowDAO;

  public WorkflowInstanceDAO(Db db, WorkflowDAO workflowDAO) {
    this.db = db;
    this.workflowDAO = workflowDAO;
  }

}
