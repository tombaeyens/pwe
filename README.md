# Principles of Workflow Engines

Every self-respecting developer has written a workflow engine in their life.  I have a lot of self respect :)

I'ld love to collect my knowledge on design principles for workflow engines.  This repo is a scratchpad in which I want to start collecting those ideas.  It might end up as a series of blog posts or a book.  But at least it's going to start as a repo.

* Minimal workflow engine
  * Basic execution
  * Automatic
  * Wait states
* Workflow models
  * Composition
  * Graph structure
* Workflow instance models
  * Activity instances
  * Tokens
* Persistence
  * Store, load and resume execution
  * Dirty checking
* Concurrency
  * Threads vs process concurrency
  * Asynchronous continuations
* Tail recursion (call stack) vs atomic operations
  * Events and persistence
* Activities
  * Configuration
  * Persistence
  * Serialization
  * Extracting behavior into an interface
  * Dynamic pluggability
* Human tasks
* Scripts
* Http requests
