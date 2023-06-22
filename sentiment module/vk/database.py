import MySQLdb

db = MySQLdb.connect(host="localhost",    # your host, usually localhost
                     user="root",         # your username
                     passwd="1234",  # your password
                     db="easyadmin")        # name of the data base


cur = db.cursor()
cur.execute("SELECT * FROM FILTER WHERE ID = 1")

# print all the first cell of all the rows
for row in cur.fetchall():
    print(row[1])

db.close()