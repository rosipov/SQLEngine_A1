import sys, os
env = Environment(
	ENV = {'PATH' : os.environ['PATH']}
)
classes = env.Java('bin', 'sqlengine_a1')
jar = env.Jar('sqlengine.jar', [classes, 'sqlengine_a1/jar-manifest.txt'])
