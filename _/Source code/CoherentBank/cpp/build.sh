#!/bin/sh

# specify the development root directory
export DEV_ROOT=.

export MODE=$1

if [ -z $COHERENCE_CPP_HOME ] ; then
	export COHERENCE_CPP_HOME="$COHERENCE_HOME$/coherence-cpp"
fi

if [ -z $COHERENCE_CPP_HOME ] ; then
	echo Check system variables. 
	echo	1. COHERENCE_HOME needs to point to the root of Coherence instalation location
	echo	and inside that folder "coherence-cpp" folder needs to contain coherence.cpp instalation
	echo    or
	echo	2. COHERENCE_CPP_HOME needs to point to the root of coherence.cpp instalation folder
	exit 1
fi
	
export OPT="-Wall -ansi"

export INC="-I$DEV_ROOT/include/seovic/samples/bank/domain"
export INC="$INC -I$DEV_ROOT/include/seovic/samples/bank/serializer" 
export INC="$INC -I$DEV_ROOT/include/seovic/samples/bank/processor" 
export INC="$INC -I$DEV_ROOT/include/seovic/samples/bank/helper"
export INC="$INC -I$DEV_ROOT/include/seovic/samples/bank/service"
export INC="$INC -I$DEV_ROOT/include/seovic/samples/bank/exception"
export INC="$INC -I$COHERENCE_CPP_HOME/include" 
export INC="$INC -I."

export SRC="$DEV_ROOT/src/seovic/samples/bank/*.cpp"
export SRC="$SRC $DEV_ROOT/src/seovic/samples/bank/domain/*.cpp"
export SRC="$SRC $DEV_ROOT/src/seovic/samples/bank/serializer/*.cpp"
export SRC="$SRC $DEV_ROOT/src/seovic/samples/bank/processor/*.cpp"
export SRC="$SRC $DEV_ROOT/src/seovic/samples/bank/helper/*.cpp"
export SRC="$SRC $DEV_ROOT/src/seovic/samples/bank/service/*.cpp"

export OUT="$DEV_ROOT/dist/atm"

export LIBPATH="-L$COHERENCE_CPP_HOME/lib"
export LIBS=-lcoherence

if [[ $MODE == "debug" ]] ; then
    export OPT="$OPT -g"
else
    export OPT="$OPT -O3"
fi

mkdir -p "$DEV_ROOT/dist"
  
echo building $OUT ...
g++ $OPT $INC $SRC $LIBPATH $LIBS -o $OUT

# copy config files
echo copying coherence cofiguration files...
cp "$DEV_ROOT/cfg/"*.xml "$DEV_ROOT/dist"
ln -sf $COHERENCE_CPP_HOME/lib/* "$DEV_ROOT/dist"
echo done