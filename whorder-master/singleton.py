
def singleton(cls):
    """
    create a singleton for target class

    Returns:
        obj: construct from the target class
    """
    _instance = {}
    def callfunc(*args, **kwargs):
        if cls not in _instance:
            _instance[cls] = cls(*args, **kwargs)
        return _instance[cls]
    return callfunc


