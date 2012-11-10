import sys, os
env = Environment(
	ENV = {'PATH' : os.environ['PATH']}
)
classes = env.Java('bin', 'SQLEngine_A1/src')
jar = env.Jar('sqlengine.jar', [classes, 'SQLEngine_A1/jar-manifest.txt'])
