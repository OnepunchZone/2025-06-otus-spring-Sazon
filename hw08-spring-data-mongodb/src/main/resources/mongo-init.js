db = db.getSiblingDB('maindb');

print('Starting database initialization...');

db.createCollection('authors');
db.createCollection('genres');
db.createCollection('books');
db.createCollection('comments');

db.authors.deleteMany({});
db.genres.deleteMany({});
db.books.deleteMany({});
db.comments.deleteMany({});

db.authors.insertMany([
    {
        _id: ObjectId("507f1f77bcf86cd799439011"),
        full_name: "Author_1"
    },
    {
        _id: ObjectId("507f1f77bcf86cd799439012"),
        full_name: "Author_2"
    },
    {
        _id: ObjectId("507f1f77bcf86cd799439013"),
        full_name: "Author_3"
    }
]);

db.genres.insertMany([
    {
        _id: ObjectId("607f1f77bcf86cd799439021"),
        name: "Genre_1"
    },
    {
        _id: ObjectId("607f1f77bcf86cd799439022"),
        name: "Genre_2"
    },
    {
        _id: ObjectId("607f1f77bcf86cd799439023"),
        name: "Genre_3"
    }
]);


db.books.insertMany([
    {
        _id: ObjectId("707f1f77bcf86cd799439031"),
        title: "BookTitle_1",
        author: DBRef("authors", ObjectId("507f1f77bcf86cd799439011")),
        genre: DBRef("genres", ObjectId("607f1f77bcf86cd799439021"))
    },
    {
        _id: ObjectId("707f1f77bcf86cd799439032"),
        title: "BookTitle_2",
        author: DBRef("authors", ObjectId("507f1f77bcf86cd799439012")),
        genre: DBRef("genres", ObjectId("607f1f77bcf86cd799439022"))
    },
    {
        _id: ObjectId("707f1f77bcf86cd799439033"),
        title: "BookTitle_3",
        author: DBRef("authors", ObjectId("507f1f77bcf86cd799439013")),
        genre: DBRef("genres", ObjectId("607f1f77bcf86cd799439023"))
    }
]);

db.comments.insertMany([
    {
        _id: ObjectId("807f1f77bcf86cd799439041"),
        text: "Comment_1",
        book: DBRef("books", ObjectId("707f1f77bcf86cd799439031"))
    },
    {
        _id: ObjectId("807f1f77bcf86cd799439042"),
        text: "Comment_2",
        book: DBRef("books", ObjectId("707f1f77bcf86cd799439031"))
    },
    {
        _id: ObjectId("807f1f77bcf86cd799439043"),
        text: "Comment_3",
        book: DBRef("books", ObjectId("707f1f77bcf86cd799439032"))
    }
]);

print('Database initialization completed successfully!');