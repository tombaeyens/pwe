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
    * https://en.wikipedia.org/wiki/Finite-state_machine
  * What's it used for
    * Business process management
    * Rapid prototyping
    * Coordinate usertasks
    * Combine user tasks and enterprise integrations
  * More generic description of a workflow:
    * Graph based diagram
    * Represents an execution flow
    * Ability to wait & resume execution (aka long running)
    * This usually implies persisting the execution state
    * What's so hard about it? 
      * Programming languages can't wait persistent
      * Programming languages can't be represented graphically

# Goals

  * Each 'language' has
    * Its own set of control and functional activities
    * Its own infrastructure
  * This work aims to identify the common workflow foundations and 
    separate them from the diverse pieces of infrastructure around 
    the engine as found in concrete workflow products or languages.
  * On the other hand, a reference implementation that maps 
    the workflow foundations so that it can be embedded into to todays 
    scalable cloud architectures.  This implies separating persistence 
    and ensuring that persistence implementations can make full advantage of 
    eventual consistent data stores.
  * It should be documented how typical workflow infrastructure can be 
    built on top of the core implementation. 

# Workflow concepts

This section should describe the basics in similar style to the concepts section in https://en.wikipedia.org/wiki/Spreadsheet 

  * Workflow
  * Activities
  * Transitions
  * Types of activities
    * Control flow activities
    * Functional activities
    * Composite activities
    * Sub workflow activity
    * Internal activities
    * External activities
    * Mixed: Sometimes activities combine functional behavior and control flow (eg all BPMN activities perform a fork at the end) 
  * Advanced workflow concepts
    * Workflow templates  
    * Listeners

TODO: * Triggers
  - take external data
  - start the execution flow
  - TODO link to activities (like BPMN start events) yet to be determined
  - one or more triggers per workflow
  - only one can be used to start one workflow instance
  - worklfow can have a default trigger 
  
# Control flow

  * Control flow means defining the sequence in which activities have to be performed
  * Mostly transitions are used to specify control flow.  A transition defines 
    a dependency between an activity that has to be completed before the next 
    activity can start.
  * Wait states
    * For some activities, work is done outside the workflow engine.
      Common example is a user task.  A user task is typically represented 
      as an activity (box) in the workflow.  When the control flow arrives at 
      the user task activity, the workflow system has to wait until the 
      task is completed by the user.
    * Another way to look at this is that the control flow leaves the workflow 
      engine to an external entity becomes responsible to send a message back 
      to the workflow engine when it's done so the workflow engine can resume.
    * This is aka 'long-running' processes   
  * Parallel paths of execution (=process concurrency)
    * Individual sequences of activities that should be accomplished
    * Forking and joining flows of execution
  * Process concurrency vs multithreaded processing
    * In theory process concurrency could be mapped 1-1 to multithreaded 
      processes, but usually that's not the case.  
    * Workflows typically are about persistent storage of the execution
    * Imagine all waiting is modeled as external activities
    * Engine calculations are fast.  We can assume that workflow instance 
      updates are always done instantly.  Whereas external activities could 
      also be done instantly, but usually they take significantly more time. 
    * Think of this as a transactional state machine
    * Process concurrency is about the dependence or independency of 
      order activity execution
    * Static vs dynamic determination of concurrency
  * Start logic
  * Data availability interrupting control flow
  * Timers: control flow in the future
 
# Runtime state

  * Usually implicit or ignored
  * Defines the specific semantics
  * 2 main, equivalent models:
    * Nested activity instances
    * Tokens

# Workflow data

  * Why workflows mostly require data? (incl a simple example)
  * Variables
  * Data types
  * References vs inline data
  * Scoping of variables
  * Conditions
  * Parameters & expressions
  * Static variables
  * Taking the host-language data types is typically not sufficient
      * Expressions
      * Auto conversions

# Implementation aspects

## Deployment and versioning

  * Most workflow products come with a tool to draw workflow diagrams.
    That's what we call the authoring format. 
  * Execution environment (workflow runtime): The system executing the workflow.
  * Analogy with sourcecode, version control, build and deployment in 
    software development.
  * Executable workflows are software.  They are deployed into an engine 
    and define the behavior of the system just like other software.
  * Workflow engines must include a versioning mechanism because 
    workflows are long-running.  

## Persistence

  * Persistence of workflow models, executable workflows and workflow instances

# Concurrency aspects

  * Pessimistic locking workflow instances is a valid option
    * Typically not much concurrent messages coming in
    * Long running work is exported to activity workers
    * This implies that concurrent workflow instance operations can be serialized
    * This means that the control flow operations like joining don't have to be 
      written in a threadsafe way.  It can be assumed that the running thread is 
      owning the workflow instance and that all other persistence operations are 
      serialized.
      
# Interacting with external services

  * Asynchronous external activity notifications
    * When passing the control flow out side the engine to an external activity, 
      this should eventually lead to a message back into the engine that 
      triggers the engine to continue the execution flow.
      Sometimes that external activity is super fast.  For that reason,  
      the workflow engine must ensure that notifications are only sent out 
      after the workflow engine persistence is up to date and ready to receive 
      that message that continues the execution flow. That's why a notification 
      the engine should first save the new state and include the list of notifications 
      for fail over.  Afterwards, the notifications can be performed and marked 
      as executed.  When the messages come in, the workflow instance can be locked 
      and execution can be continued.
    * Alternative: pass transaction sequence number externally so when the message comes back, 
      This way the incoming message can wait till at least the transaction number is available.
    * External activity notifications can also be used to perform 
      asynchronous activities.  Like sending an email etc.  
    * This should make asynchronous continuations unnecessary... I think :)
  * Activity worker pattern
    * Essentially this means storing external activity notifications 
      in a collection, table or queue, and then have a collection of 
      competing consumers performing the activity work and signalling completion back 
      to the engine
  * Correlation of the incoming signal with the waiting activity instance: 
    * A workflow-engine-generated reference to the activity instance can be passed to the external service.  the external service then has to pass it back when signalling completion of the external activity.
    * Alternatively, the activity can set business data on the async continuation. (aka correlation data).  That way, the external service doesn't have to have the engine generated reference to the activity id.  But it can find the async continuation based on the business data it already has.
    * BPEL style correlation
    * Activity instance correlation for continuation messages (makes sense)
    * Trigger correlation for starting a process based on arbitrary messages (doesn't make sense ==> use an event listener inbetween that listens and converts) 

# Activity configuration

  * Static value configurations: value configured in the worklfow
  * Dynamic configurations: input value based on workflow data
  * Application configurations: value coming from the application in which the engine runs (eg. task service, database connection etc)
  * Parameters, expressions and contexts  
    * Activities describe which input and output parameters they have
    * They are implemented like function calls with fixed names for inputs and outputs
    * During process authoring, inputs and outputs are bound to workflow variables  

---
Other implementation details
----

  * Exception handling
    * Programming language level exceptions occur because of bugs, unvailability of services, wrong workflow configuration, etc
    * Workflow language can define it's own exception handling (like BPMN events) 
    * Programming language level exceptions, but also plain code logic can lead to workflow language exceptions
    * Programming language level exceptions might be handled by logging (see below) and then parking the workflow instance into an error state so that an admin can intervene.
  
  * UI pluggability requires a lot of information
    * Name, description, icon 
    * A description of the configuration options.  UI will produce a config form.
    * A description of the input/output parameters.
    * Type descriptors for input/output parameters for expression dereferencing
    * Workflow variables become like an ESB
    
  * Logging
    * Activities should be able to add logging for debugging, runtime trouble shooting

  * Minimal workflow engine as an example
    * Tail recursion (call stack) vs atomic operations --> new minimal version (ch01.2) with atomic operations?

# Workflow engine data

  * Why does a BPM system need it's own typing?
    * which one?  json, java or something else?
    * BPM needs many rich data types like user(reference), file(reference), money etc
    * BPM needs more abstract data types (byte,short.int,long,float,double,...) --> number 
    * expressions
    * conversions
  * Type pluggability (extending the type structure in java)
  * User defined types: data structure for type pluggability
  * Activating activities on conditions
  * Binding message data to triggers and messages
  * Mapping json and xml to data and expressions

# Workflow engine API

  * deploy a workflow
    * workflow versioning
    * instance migration
    * workflow part of a larger application? 
  * start a workflow instance
  * send a message to signal the end of an activity instance
  * create a new data type dynamically
  * create a new activity type dynamically

# Implementation aspects

  * Multi tenancy: how to layer it on top
  * Access control: how to layer it on top
  * Immutable, serializable workflow instance copies
    * When the workflow instance object is passed for asynchronous work,
      the client should get an immutable, serializable snapshot of the 
      workflow instance.
    * Alternatively an optional visitor could be passed in the engine 
      that walks the workflow instance model before work goes 
      asynchronous.   
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
    * Change logs
    * Extending the workflow engine model
      * Eg organizationId, access control
      * Separate collections vs generic workflow model extension 
    * Combining change logs with the other approaches
    * Copying the workflow structure in the workflow instance
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
    
# Building blocks

  * Relational DBs
  * NoSQL DBs
  * Message queues
  * Persistent timers

# Scaling

  * From simplest example: finite state machine persisted as a string field in an entity inside the user's domain model
  * To Facebook scale
  * Challenge: can we build a common basis that works for all these

# Architecture

![Architecture](java-ch03/src/docs/Architecture.png "Architecture")

# Links 

  * http://blog.acolyer.org/2015/09/08/out-of-the-fire-swamp-part-i-the-data-crisis/
  * http://blog.acolyer.org/2015/09/09/out-of-the-fire-swamp-part-ii-peering-into-the-mist/
  * http://pegasus.isi.edu/
  * http://www.taverna.org.uk/
