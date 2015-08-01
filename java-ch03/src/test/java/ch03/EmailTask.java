package ch03;


public class EmailTask extends Scope {
  
  

  @Override
  public void start(ExecutionController controller) {
    
    controller.get("attachments");  
  }

  
}
