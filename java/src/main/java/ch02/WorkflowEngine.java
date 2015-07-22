package ch02;


public class WorkflowEngine {

  Db db;
  WorkflowDAO workflowDAO;
  WorkflowInstanceDAO workflowInstanceDAO;
  long nextWorkflowId = 1;
  long nextWorkflowInstanceId = 1;
  
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

  protected String generateNextWorkflowInstanceId() {
    return Long.toString(nextWorkflowInstanceId++);
  }

  public DbWorkflowInstance startWorkflowInstance(String workflowId) {
    DbWorkflow workflow = workflowDAO.findWorkflowById(workflowId);
    DbWorkflowInstance workflowInstance = new DbWorkflowInstance(workflow);
    workflowInstance.setId(generateNextWorkflowInstanceId());
    // Starting the workflow instance means executing the start activities
    // These activities may propagate the execution forward
    // Eventually a wait state will be reached or the workflow instance will end,
    // then the method returns
    workflowInstance.start();
    
    // When the workflow instance is done starting, it is in a wait state 
    // and hence we should save it
    workflowInstanceDAO.saveWorkflowInstance(workflowInstance);
    
    return workflowInstance;
  }

}
