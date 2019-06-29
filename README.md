# Mapped Diagnostic Context (MDC) with Reactor

Here is example code of how to use MDC with Reactor.

When adding info to the subscriber context,
make sure to do it from the bottom of the stream.
The context is pushed up the stream with the requests.