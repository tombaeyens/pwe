# Principles of Workflow Engines

Every self-respecting developer has written a workflow engine in their life.  I have a lot of self respect :)

This is an effort to collect my knowledge on design principles for workflow engines.  This repo is a scratchpad 
in which I want to start collecting those ideas.  It might end up as a series of blog posts or a book.  But at 
least it's going to start as a repo.

# The basics 

* Introduction
  * What is a workflow engine
    * A system that can execute graphs
  * What's it used for
    * Business process management
    * Rapid prototyping
    * Coordinate usertasks
    * Combine user tasks and enterprise integrations
  * More generic description of use cases:
    * Graph based diagram
    * Represents an execution flow
    * Async continuations (the execution flow may go outside the engine's control.
      activities may have to wait till the external service passes the execution 
      flow back to the engine)
  * What's so hard about it? 
    * Persistence during asynchronous continuations
    * Resume execution flow
    * Observation: Workflow execution is always non blocking (kinda like reactive programming)
      Cause if it would be blocking, that means you have to introduce an asynchronous continuation. 
  * Data
    * Why workflows mostly require data?
    * Examples
    * References vs inline data

* Workflow engine concepts
  * Minimal workflow engine
    * Tail recursion (call stack) vs atomic operations
  * Workflow models
    * Activity composition
    * Activity configuration
    * Transitions
    * Workflow templates  
    * Start logic
    * Listeners?
  * Workflow instance models
    * Activity instance composition
    * Token / execution based approach
    * Change logs
    * Combining change logs with the other approaches
    * Copying the workflow structure in the workflow instance
  * Data
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

* Asynchronous continuations
  * Describe the programmatic flow: activity doesn't propagate further, marks the 
    activity instance as 'waiting' or 'external'
  * Correlation: 
    * A workflow-engine-generated reference to the activity instance can be passed to the external service.  the external service then has to pass it back when signalling completion of the external activity.
    * Alternatively, the activity can set business data on the async continuation. (aka correlation data).  That way, the external service doesn't have to have the engine generated reference to the activity id.  But it can find the async continuation based on the business data it already has.

* Concurrency
  * Process concurrency vs software concurrency
  * The thread of the client can be used to execute (=interpret) the workflow
    * Tradeoff: duration vs process execution information
    * Process execution information example: which tasks have been created
    * Alternative: Futures
  * Asynchronous continuations
  * Asynchronous operations (notifications etc)
  * Message queues are an implementation technique
    * Relation to transactions

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
  * Analytics
    * Depends on the data store
    * Often, runtime data structures are not optimal
    * Duplication  
    * Can be based on listener or transaction logs
  * Archiving

* Messaging

* Timers

* BPEL style correlation
  * Activity instance correlation for continuation messages (makes sense)
  * Trigger correlation for starting a process based on arbitrary messages (doesn't make sense ==> use an event listener inbetween that listens and converts) 