# Principles of Workflow Engines

Every self-respecting developer has written a workflow engine in their life.  I have a lot of self respect :)

This is an effort to collect my knowledge on design principles for workflow engines.  This repo is a scratchpad 
in which I want to start collecting those ideas.  It might end up as a series of blog posts or a book.  But at 
least it's going to start as a repo.

# The basics 

* Minimal workflow engine
  * Basic execution
  * Automatic
  * Wait states
* Persistence
  * Store, load and resume execution
  * Transactions (advanced)
  * Implications of non-transactional persistence  (advanced)
  * Consistency (advanced)
    * Keeping the persisted process state in sync with performed side effects
    * Transactional and non transactional side effects
    * Idempotent side effects
* Concurrency
  * The thread of the client can be used to execute (=interpret) the workflow
    * Tradeoff: duration vs process execution information
    * Process execution information example: which tasks have been created
    * Alternative: Futures
  * Threads vs process concurrency
  * Asynchronous continuations
  * Asynchronous operations (notifications etc)
  * Message queues are an implementation technique
    * Relation to transactions

# Extending the basics

* Workflow models
  * Composition
  * Graph structure
  * Activity configuration  
* Workflow instance models
  * Activity instances
  * Tokens
  * Transaction log
  * Combining transaction logs with the other approaches
  * Copying the workflow structure in the workflow instance
* Data
  * Type pluggability (extending the type structure in java)
  * User defined types: data structure for type pluggability
  * Data vs references
  * Expressions: resolving an expression based on a context
  * Hierarchical contexts: scopeInstance context, scope context and external context   
  * Bindings : mapping fixed names to configurable inputs for activities
  * Conditions : boolean expressions used for:
    * Transitions (and to lesser extend activities)
  * Activating activities on conditions
  * Binding message data to triggers and messages
  * Mapping json and xml to data and expressions

# Examples
  
* Human tasks
* Scripts
* Http requests
    
# Advanced

* Advanced persistence
  * Dirty checking
  * Analytics
    * Depends on the data store
    * Often, runtime data structures are not optimal
    * Duplication  
    * Can be based on listener or transaction logs
  * Archiving
* Tail recursion (call stack) vs atomic operations
  * Events and persistence
* Activities advanced
  * Configuration
  * Persistence
  * Serialization
  * Extracting behavior into an interface
  * Dynamic pluggability
* Event based alternative
  * TODO explore
  
