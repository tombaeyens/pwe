# Principles of Workflow Engines

Every self-respecting developer has written a workflow engine in their life.  I have a lot of self respect :)

This is an effort to collect and share my knowledge on design principles for implementing workflow 
engines.  This repo is a scratchpad in which I am starting to collect and document ideas.  It might 
end up as a series of blog posts or a book.

# Introduction

  * What is a workflow engine
    * A software system that can execute graphs
    * https://en.wikipedia.org/wiki/Business_Process_Model_and_Notation
    * https://en.wikipedia.org/wiki/Flowchart
    * https://en.wikipedia.org/wiki/Petri_net
  * What's it used for
    * Business process management
    * Rapid prototyping
    * Coordinate usertasks
    * Combine user tasks and enterprise integrations
  * More generic description of a workflow:
    * Graph based diagram
    * Represents an execution flow
    * Async continuations (the execution flow may go outside the engine's control.
      activities may have to wait till the external service passes the execution 
      flow back to the engine)
  * What's so hard about it? 
    * Programming languages can't wait
    * Persistence during asynchronous continuations
    * Resume execution flow
    * Graph based
    * Observation: Workflow execution is always non blocking (kinda like reactive programming)
      Cause if it would be blocking, that means you have to introduce an asynchronous continuation. 

# Workflow engine concepts

  * Minimal workflow engine as an example
  * Workflow models
    * Activity composition
    * Activity configuration
    * Transitions
    * Workflow templates  
    * Start logic
    * Listeners?
  * Types of activities
    * Control flow activities
    * Functional activities (aka side effects)
    * Combinations of control flow and functional activities
  * Workflow instance models
    * Activity instance composition
    * Token / execution based approach
    * Change logs
    * Combining change logs with the other approaches
    * Copying the workflow structure in the workflow instance

# Concurrency aspects

  * The thread of the client can be used to execute (=interpret) the workflow
    * Tradeoff: duration vs process execution information
    * Process execution information example: which tasks have been created
    * Alternative: Futures
  * Activity asynchronous options
    * Synchronous/Asynchronous start of the activity
    * Asynchronous start used for external services that take too long for 
      the client request to wait. The external service invocation can be 
      the actual work, or it can be a notifications that eventually will trigger a callback
    * Is there an incoming external signal that continues the workflow execution flow
  * Asynchronous continuations
  * Message queues are an implementation technique
    * Relation to transactions
  * Activity categories
    * Synchronous, internal activities
      * In the thread of the client
      * Work being done inside the engine
      * eg control flow activities, assignment
    * Synchronous external activities
      * Starts in the thread of the client
      * Wait state that requires activity instance message to continue
      * eg User Task
    * Asynchronous internal activities
      * Started asynchronous
      * Work being done inside the engine
      * eg JavaScript, Send Email
    * Asynchronous external activities
      * Started asynchronous
      * Wait state that requires activity instance message to continue
      * eg Request-response interaction over message queues
  * Process concurrency vs software concurrency

# Workflow engine data

  * Why workflows mostly require data?
  * Examples
  * References vs inline data
  * Why does a BPM system need it's own typing?
    * which one?  json, java or something else?
    * BPM needs many rich data types like user(reference), file(reference), money etc
    * BPM needs more abstract data types (byte,short.int,long,float,double,...) --> number 
    * expressions
    * conversions
  * Type pluggability (extending the type structure in java)
  * User defined types: data structure for type pluggability
  * Data vs references
  * Dereferencing references vs dereferencing structured, inlined variable data
  * Static variables
  * Expressions: resolving an expression based on a context
  * Hierarchical contexts: scopeInstance context, scope context and external context   
  * Bindings : mapping fixed names to configurable inputs for activities
  * Conditions : boolean expressions used for:
    * Transitions (and to lesser extend activities)
  * Activating activities on conditions
  * Binding message data to triggers and messages
  * Mapping json and xml to data and expressions

# Interacting with external services

  * Data mapping through parameters/bindings/expressions
  * Hard coded service invocations
    * Can use variables directly
  * Perform external service notifications after saving the new workflow state.
    * When notifying an external service, make sure that the 
      engine does this after the persistence of the current 
      operarations has been completed and persisted.
      Because the external service might be really quick.
      And it might signal before the engine persists the 
      new state.
  * Generic input and output parameters
    * Activities describe which input and output parameters they have
    * They are implemented like function calls with fixed names for inputs and outputs
    * During process authoring, inputs and outputs are bound to workflow variables  
  * Correlation of the incoming signal with the waiting activity instance: 
    * A workflow-engine-generated reference to the activity instance can be passed to the external service.  the external service then has to pass it back when signalling completion of the external activity.
    * Alternatively, the activity can set business data on the async continuation. (aka correlation data).  That way, the external service doesn't have to have the engine generated reference to the activity id.  But it can find the async continuation based on the business data it already has.

# Implementation aspects

  * Tail recursion (call stack) vs atomic operations
  * Workflow engine API
    * deploy a workflow
    * start a workflow instance
    * send a message to signal the end of an activity instance
  * Persistence
    * Store, load and resume execution
    * Transactional workflow instance state changes
    * Activity side effects 
    * Workflow instance updates vs side effects
    * Non-transactional persistence
      * Saving
    * Consistency (advanced)
      * Keeping the persisted process state in sync with performed side effects
      * Transactional and non transactional side effects
      * Idempotent side effects
    * Dirty checking
    * Archiving
  * Collecting analytics
    * Logs (jbpm3, activiti dumping logs in elastic search)
    * Star based relational schema (jbpm4+)
    * Most workflow engines generate statistical reports
    * Some overlap with runtime data structures
    * Depends on the data store
    * Often, runtime data structures are not optimal
    * Duplication  
    * Can be based on listener or transaction logs
  * Access control
  * BPEL style correlation
    * Activity instance correlation for continuation messages (makes sense)
    * Trigger correlation for starting a process based on arbitrary messages (doesn't make sense ==> use an event listener inbetween that listens and converts) 
    
# Building blocks

  * Relational DBs
  * NoSQL DBs
  * Message queues
  * Persistent timers
