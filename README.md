Recreate [#8722][10]
--

The project has two endpoints
- `/ping-async` runs a task on an executor service with helidon wrapping and does some logging from it
- `/otel/ping-async` runs a task on an executor service with otel and helidon wrapping and does some logging from it 

When running `/ping-async` you can see the following lines in the logs

```
11:39:41.223 [[0x6492faf9 0x44a9ffd6] WebServer socket] INFO  uk.co.borismorris.PingService {span_id=05d5aba78f4fb166, trace_flags=01, trace_id=cd40a894c4e06e576910d72b1b943922} - Async ping request received
11:39:41.224 [] INFO  uk.co.borismorris.PingService {} - Running async ping request
```

The first line, from the request thead, clearly has a trace id. The second line, from the async task, is missing all trace context.

---

When running `/otel/ping-async` you can see the following lines in the logs

```
11:47:54.638 [[0x3d7b2f1c 0x79e0bfff] WebServer socket] INFO  uk.co.borismorris.PingService {span_id=9cf77ed194860af8, trace_flags=01, trace_id=f9be44c8eef91fa3f266e640f4c34edf} - Async ping request received
11:47:54.639 [] INFO  uk.co.borismorris.PingService {span_id=9cf77ed194860af8, trace_flags=01, trace_id=f9be44c8eef91fa3f266e640f4c34edf} - Running async ping request
```

The first line, from the request thead, clearly has a trace id and the second line hass the same id.

[10]: https://github.com/helidon-io/helidon/issues/8722
