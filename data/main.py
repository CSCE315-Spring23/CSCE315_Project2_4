import csv

with open('ItemIngredients.csv') as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')
    line_count = 0
    for row in csv_reader:
        if line_count == 0:
            print(f'Column names are {", ".join(row)}')
            line_count += 1
        else:
            itemID = row[0]
            name = row[1]
            ingredients = row[2].split(", ")
            # print(f'itemID: \t{row[0]}, name {row[1]}, ingredients {row[2].split()}.')
            line_count += 1
            print(f'INSERT INTO itemingredients (itemid, name, ingredients) values ({row[0]}, \'{row[1]}\', ARRAY {ingredients});')
    print(f'Processed {line_count} lines.')