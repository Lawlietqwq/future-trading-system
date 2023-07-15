import pymysql


def db_connect(cursor=pymysql.cursors.DictCursor):
    db = pymysql.connect(host='csubigdata.com', user='root', password='Bigdata.db@2021', port=3306)
    cursor = db.cursor(cursor)
    return db, cursor
