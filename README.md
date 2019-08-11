# Signal
A Java library to provide signalling between threads.

The `Signal` can be set or unset at any time. Threads can
wait for the signal to become set, or can peek at the signal.

This implements the necessary concurrency patterns to work in most situations.

For an explanation see the post:
 https://codingcraftsman.wordpress.com/2015/07/27/signalling-between-threads-in-java/
