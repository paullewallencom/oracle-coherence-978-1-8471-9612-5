Deployment Instructions for Coherent Bank Sample Application

------------------------------------------------------------------------------
01. Operating System Requirements
------------------------------------------------------------------------------

CoherentBank sample application is configured to run on Windows XP out of the
box. While individual developers have run Java application on Mac OS X as well
during development, it is not been extensively tested on platforms other than
Windows XP.

Also, you will only be able to run .NET client application on Windows, so it
is definitely the best fit for evaluation. Once you become more familiar with
the application architecture, you should be able to modify necessary
configuration files and to deploy the application on multiple machines, and
multiple operating systems.

However, running everything on a single Windows XP box is by far the easiest
way to get started.


------------------------------------------------------------------------------
02. Prerequisites
------------------------------------------------------------------------------

In order to build and deploy various components of CoherentBank sample
application, you will need the following:

a) Java JDK 1.6

Any update or revision of the 1.6 JDK should do, but make sure that you have
full JDK installed, not just the JRE. You will also need to set JAVA_HOME
environment variable to point to your JDK. If you installed JDK using into a
default location, your JAVA_HOME should look similar to the following:

JAVA_HOME=C:\Program Files\Java\jdk1.6.0_07

b) Microsoft Visual Studio 2008

You will need Visual Studio 2008 to build .NET and C++ sample applications.
.NET Framework 3.5 will be installed along with it.

c) Oracle Coherence 3.5.3

The application should work with other Coherence versions as well, but it has
been tested with 3.5.3, so it is strongly recommended that you use that
release.

You will need to download and install several packages:

* Download Coherence Java release and install it according to instructions in
Chapter 2. Set COHERENCE_HOME environment variable to point to your
installation directory containing bin, lib and other Coherence folders.

* Download and install Coherence for C++ 32-bit Windows release. You should
install it by extracting coherence-cpp directory from the downloaded archive
into the COHERENCE_HOME directory. For example, if your COHERENCE_HOME is
C:\coherence, your Coherence for C++ installation should be in
C:\coherence\coherence-cpp.

* Download and install Coherence for .NET. Simply run the installer from the
download archive and accept the installation defaults when prompted. Make sure
that you use the default installation path, as that’s the location used to
find Coherence.dll when building .NET sample application.

d) Ant 1.7.x+

You will need to have Ant installed and in your system path in order to build
and deploy the application. Make sure that you set ANT_HOME environment
variable to Ant installation directory, and that you add ANT_HOME\bin to your
PATH.

e) NAnt 0.86+

You will need NAnt to build .NET sample app. Make sure that you add NAnt’s bin
directory to PATH as well.


------------------------------------------------------------------------------
03. Installation
------------------------------------------------------------------------------

In order to install sample application, simply unzip CoherentBank.zip
somewhere on your disk. You should have CoherentBank directory with three
subdirectories: cpp, java and net.


------------------------------------------------------------------------------
04. Deploying Java Application
------------------------------------------------------------------------------

a) Open command prompt and navigate to CoherentBank\java directory.

b) Edit build.properties file to reflect your environment

c) Start the H2 database:

> ant start-db-server

If everything goes well, the H2 console should open in the browser. You can
log in by changing JDBC URL to ‘jdbc:h2:tcp://localhost/db/coherent-bank-db’,
setting username to ‘sa’ and clicking ‘Connect’ button. There will be no
tables there at first, which will be corrected shortly.

d) Start web application

> ant web-app

This should run the SQL script to create necessary tables, build the
application, start embedded Jetty web server and open up the web browser
showing the login screen.

If the web server fails to start make sure that you don’t have anything else
running on port 8080 and retry (or alternatively, change ‘jetty.port’ property
in build.xml file).

If the browser does not open automatically, make sure that you have specified
correct browser location in build.properties file, or open browser manually
and navigate to ‘http://localhost:8080/bank/login’.

e) Log in using one of the test username/password combinations or create an
account for yourself by clicking on ‘register here’ link below login form.

Built-in test users are: sele, aca, marko, ivan, mark and patrick, and
password for all of them is ‘pass’ (without quotes).

f) Play with the application and post some transactions by paying bills.

g) Start Coherence Extend proxy server:

> ant start-extend-proxy

This will allow C++ and .NET client to connect to the cluster.


*** (optional)

By default, web application runs as a storage enabled Coherence node to
simplify deployment, even though that is not how you would run it in
production.

If you want to simulate a more realistic environment, uncomment the
‘-Dtangosol.coherence.distributed.localstorage=false’ JVM argument within
‘start-web-server’ target in build.xml.

However, if you do this you will need to run one or more cache servers before
the step d) above. You can do that by changing startup sequence to:

> ant start-db-server 
> ant start-cache-server (one or more times) 
> ant web-app 
> ant start-extend-proxy

***


------------------------------------------------------------------------------
05. Deploying C++ Application
------------------------------------------------------------------------------

a) Open Visual Studio 2008 Command Prompt and navigate to CoherentBank\cpp
directory. This will ensure that all environment variable necessary to ron
Microsoft C++ compiler are properly configured.

b) Build the ATM application:

> build

This should compile and link the application and copy the executable and
necessary configuration files into ‘dist’ subdirectory.

c) Navigate to ‘dist’ subdirectory.

d) Run the application with no arguments:

> atm

This should print out usage instructions:

Usage: atm <deposit|withdraw> <account number> <amount> <currency code>

e) Run the application one or more times against one of the accounts you have
access to (can see it in the web application when logged in). For example, if
you want to withdraw $200 from account with the ID 10 run the following:

> atm withdraw 10 200 USD

You should see the response similar to the following:

Transaction Details 
---------------------------------- 
Type:        WITHDRAWAL
Description: ATM withdrawal ($ 200.00 @ 0.6630) 
Amount:      132.6 EUR
Balance:     20745.4 EUR

f) Do several withdrawals and deposits against the same account and then view
account transactions in the web application.

*** 
Note: The only currencies supported at the moment are USD, EUR, GBP, CHF,
AUD, CAD and JPY. 
***


------------------------------------------------------------------------------
06. Deploying .NET Application
------------------------------------------------------------------------------

a) Open command prompt and navigate to CoherentBank\net directory.

b) Build and run BranchTerminal sample application:

> nant run-terminal

This should build and start .NET client and display a list of all the
accounts, grouped by customer. You can click on account to see a list of
transactions on the right.

c) Post some transactions by paying bills from the web application and by
making ATM withdrawals and deposits using C++ application and watch how
account balance and transaction list are updated in real-time.


------------------------------------------------------------------------------
07. Shutting Everything Down
------------------------------------------------------------------------------

a) Close BranchTerminal .NET application if running.

b) Navigate to CoherentBank\java directory and stop the Coherence cluster:

> ant stop-cluster

This will shut down all web server, proxy server and all cache servers you
started.

c) Stop database server:

> ant stop-db-server

This *should* shut down database server, but it often doesn’t for some reason.
You might need to open Task Manager and terminate java.exe process for the
database server manually (if you have multiple java.exe processes running
after you stopped the cluster, look for one that uses around 30 MB of memory).


------------------------------------------------------------------------------
08. Look at the Code
------------------------------------------------------------------------------

The main reason for sample application’s existence is not the functionality it
provides (which is fairly limited), but the implementation code behind it that
demonstrates some of Coherence best practices.

By reviewing the sample code while reading the book (and after), you will gain
a lot more than by simply reading the book (or the code) alone.

You should also download the latest Coherence Tools release from
http://code.google.com/p/coherence-tools/, as that’s where most of the base
and utility classes used in the sample application are.

Reviewing Coherence Tools code along with the sample application code that
depends on it is highly recommended. It will also give you most “bang for the
buck”, as you’ll be better able to leverage full Coherence Tools feature set
on your next project.

Enjoy!