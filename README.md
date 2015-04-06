# Futures #

Example code on the usage of the [Twitter Futures api](https://twitter.github.io/finagle/guide/Futures.html) using executable tests.

I created this project when I started using [Finagle](http://twitter.github.io/finagle/) which
makes heavily use of the futures api. It helped my to get familiar with the api without relying on business cricital code.

The tests also help to see sequential vs concurrent execution by executing tests sequentially and logging what happens.

It was useful for me and can be useful for others starting with the Twitter or [Scala Futures api](http://docs.scala-lang.org/overviews/core/futures.html).  It does by no means cover the
complete functionality of the futures api but I implemented the cases I came across and started using myself in production code.


The project uses [sbt](http://www.scala-sbt.org/). So if you have sbt installed you should be able to execute:

`sbt compile` : Compiles application.

`sbt test:compile` : Compiles tests.

`sbt test` : Executes the tests.

`sbt eclipse` : Create Eclipse project files.

`sbt gen-idea` : Create IntelliJ IDEA project files.
