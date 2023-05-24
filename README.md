
## Workflow triggering
* On Commit
> From the investigation I did I discovered that there are two ways to trigger a pipeline when a commit occurred.
> 
> <br>**First** is by pulling, basically our application has a list of repositories with corresponding branches which should be monitored 
> in a fixed interval (i.e. every 15 seconds), whenever a new commit was detected, pipeline is triggered.
> <br>**Pros:** Easy to set up (no need in extra actions from the client),
> <br>**Cons:** 1. A bit of delay before pipeline is triggered, 2. Required **orchestration**/**deduplication logic** when runs in multi-node setup, 3. More expensive (in terms of resource usage), since does extra HTTP calls to VCS.
> 
> **Second** is by pushing (webhook), whenever a commit occurs, client is responsible to invoke an HTTP endpoint with metadata (commit,branch,repo)
> ![service_hld.png](docs/images/service_hld.png)
> <br>**Pros:** 1.Easily can run in multi-node setup (without any orchestration in place), since request gets forwarded by LB to a single node, 2. Fewer resources are being used since application is not doing any extra calls. 
> <br>**Cons:** 2.Requires client to implement the webhook logic on his side.

* On schedule
> To trigger the workflow, all scheduled requests have to be store in a database, the most important columns 
> would be the **cron_expression** and **last_execution_date_time**, using these fields application can schedule internally tasks which will push the message to the queue following required delay/time 
> , to guarantee that job is scheduled just once in a multi-node setup, an optimistic-lock will be used since expected number of collisions is small.
> To achieve this behavior out of the box **jobrunr** or **Quartz** libraries can be used, in this project **jobrunr** was used.
> 
> **How jobrunrs handles multi-node setup:**
> JobRunr uses optimistic locking to make sure that a job is only processed once. Concretely, this means that 
> when a server starts processing a job, it first changes the state to PROCESSING and tries to save that to the database. 
> If that fails, it means that the job is already processing by another server and the current server will not process it again.
> If it succeeds, it means that the job is not being processed by another server and the current server can process it.
## Things which can be extended
Since the solution I provided is just a show-case of how I write code, tackle the problems and in general how I think,
some of the in depth things I left incomplete on purpose (and wrote comments how I'd handle it in a real life scenario). (like validation, user-experience, etc.) 
**What I'd improve**
1. Add more validations steps
2. Allow customers to create scheduled build using  Intervals(i.e. P5D), Date Time, etc.

### Side notes :
1. Some javadocs contains section that starts with: `side_node:`, these notes 
are just for you so you can have a better understanding how I was thinking, in the production code I won't add them. 