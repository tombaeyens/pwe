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
* Concurrency
  * Observation: The thread of the client is used to execute (=interpret) the workflow
  * Threads vs process concurrency
  * Asynchronous continuations

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
* Data
  * Type pluggability
  * Data in the workflow model
    * Configuration of pluggable activities
  * Scoped contexts
  * Data vs references
  * Expression resolving
    * Expression resolving of referenced objects
  * Data unavailability blocking execution 
* Event based alternative
  * TODO explore
  