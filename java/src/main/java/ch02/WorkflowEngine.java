package ch02;


public class WorkflowEngine {

  Db db;
  WorkflowDAO workflowDAO;
  WorkflowInstanceDAO workflowInstanceDAO;
  long nextWorkflowId = 1;
  
  public WorkflowEngine() {
    db = new Db();
    workflowDAO = new WorkflowDAO(db);
    workflowInstanceDAO = new WorkflowInstanceDAO(db, workflowDAO);
  }

  public String deployWorkflow(DbWorkflow workflow) {
    String workflowId = generateNextWorkflowId();
    workflow.setId(workflowId);
    workflowDAO.saveWorkflow(workflow);
    return workflowId;
  }

  protected String generateNextWorkflowId() {
    return Long.toString(nextWorkflowId++);
  }

  public DbWorkflowInstance startWorkflowInstance(String workflowId) {
    workflowDAO.findWorkflowById(workflowId);
    return null;
  }
}
