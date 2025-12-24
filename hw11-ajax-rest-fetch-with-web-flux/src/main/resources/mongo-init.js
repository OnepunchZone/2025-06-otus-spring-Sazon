db = db.getSiblingDB('maindb');

print('Starting database init...');

db.authors.deleteMany({});
db.genres.deleteMany({});
db.books.deleteMany({});
db.comments.deleteMany({});

db.authors.insertMany([
    { _id: "1", full_name: "Author_1" },
    { _id: "2", full_name: "Author_2" },
    { _id: "3", full_name: "Author_3" }
]);

db.genres.insertMany([
    { _id: "1", name: "Genre_1" },
    { _id: "2", name: "Genre_2" },
    { _id: "3", name: "Genre_3" }
]);

db.books.insertMany([
    {
        _id: "1",
        title: "BookTitle_1",
        author: { _id: "1", full_name: "Author_1" },
        genre: { _id: "1", name: "Genre_1" },
        comments: [
                    { _id: "1", text: "Comment_1" },
                    { _id: "2", text: "Comment_2" }
                ]
    },
    {
        _id: "2",
        title: "BookTitle_2",
        author: { _id: "2", full_name: "Author_2" },
        genre: { _id: "2", name: "Genre_2" },
        comments: [
                    { _id: "3", text: "Comment_3" }
                ]
    },
    {
        _id: "3",
        title: "BookTitle_3",
        author: { _id: "3", full_name: "Author_3" },
        genre: { _id: "3", name: "Genre_3" },
        comments: []
    }
]);

db.comments.insertMany([
    { _id: "1", text: "Comment_1", bookId: "1" },
    { _id: "2", text: "Comment_2", bookId: "1" },
    { _id: "3", text: "Comment_3", bookId: "2" }
]);

print('database init completed!');