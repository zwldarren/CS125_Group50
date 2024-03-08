from engine import Engine


def main(argv):
    engine = Engine(".")
    engine.process_query(argv)
    print(argv)
