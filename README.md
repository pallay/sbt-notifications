# Growling Tests

An [sbt](https://github.com/harrah/xsbt#readme) 0.13.* plugin that notifies test results to the operating system. Useful for red/green testing.

## Install

### On Mac

#### Option 1
Use the default notification build into the Mac OS

#### Option 2
Install the [growl](http://growl.info/) extra [growlnotify](http://growl.info/extras.php#growlnotify) commandline client, make sure it's in your path and you have growl turned on.

### On Ubuntu

Install the `libnotify-bin` package.

    sudo apt-get install libnotify-bin

### On Windows (7) / GIT bash

Install the `growl for windows` package. This is availble from [growl for windows](http://www.growlforwindows.com/gfw/help/growlnotify.aspx). This must be on the windows path. Note that the usage of growlnotify has only been tested from cygwin (specifically GIT bash). The sbt growl plugin uses growlnotify.exe, not growlnotify.com.

### Project Configuration

To install on a per-project basis, add the following to your plugin definition file

    addSbtPlugin("com.raunu" % "sbt-growl-plugin" % "0.1.0")

    resolvers += Classpaths.sbtPluginReleases

To install globally, create a `Build.scala` file under `~/.sbt/plugins/project` directory and add the following

    import sbt._
    object PluginDef extends Build {
      override def projects = Seq(root)
      lazy val root = Project("plugins", file(".")) dependsOn(growl)
      lazy val growl = uri("git://github.com/pallay/sbt-notify.git")
    }

Run your tests with the sbt `test` or `~test` task.

## Configuring Icons

By default the plugin looks for icons in `~/.sbt/sbt-notify/icons/`. Specifically, it looks for:

* `pass.png` - used when tests pass
* `fail.png` - used when tests fail
* `error.png` - used for catastrophic failures

If a notification icon is not found, the plugin extracts a default logo one and places it in `~/.sbt/sbt-notify/icons/`.

The directory which is inspected for icons can be configured by adding this to your `build.sbt` file:

    defaultImagePath := "/my/better/path"

You can configure images individually by reconfiguring the TestImages class. For example:

    (NotifyKeys.images in NotifyKeys.Notify) <<= (NotifyKeys.images in NotifyKeys.Notify)(i => i.copy(fail = Some("/better/fail/icon.png")))


## Props

This plugin is based on work from softprops and jsuereth using a BDD approach from Ruby on Rails.