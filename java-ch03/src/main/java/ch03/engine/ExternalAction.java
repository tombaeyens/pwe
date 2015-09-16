package ch03.engine;



/** notifications to propagate the flow to activity workers.
 * These notification are performed after there is no more 
 * synchronous work on the workflow instance.  The notification 
 * will be performed after the workflow instance is persisted and 
 * unlocked.  This way, external activity workers can be sure that 
 * the workflow instance is available if they want to send 
 * a message back. 
 * 
 * @author Tom Baeyens
 */
public interface ExternalAction {

  void executionEnded(Context context);
}
