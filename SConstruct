import sys, os
env = Environment(
	ENV = {'PATH' : os.environ['PATH']}
)

classes = env.Java('bin', 'sqlengine_a1')
jar = env.Jar('sqlengine.jar', [classes, 'sqlengine_a1/jar-manifest.txt'])
parser = env.Command('sqlengine_a1/parser/SqlParser.java', [], 'python ./scripts/generate_parser.py >$TARGET')
env.Depends(parser, './scripts/generate_parser.py')
env.Depends(classes, parser)
