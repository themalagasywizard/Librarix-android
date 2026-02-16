package com.librarix.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.librarix.data.local.entity.BookEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BookDao_Impl implements BookDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BookEntity> __insertionAdapterOfBookEntity;

  private final EntityDeletionOrUpdateAdapter<BookEntity> __deletionAdapterOfBookEntity;

  private final EntityDeletionOrUpdateAdapter<BookEntity> __updateAdapterOfBookEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteBookById;

  public BookDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBookEntity = new EntityInsertionAdapter<BookEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `books` (`id`,`title`,`author`,`description`,`coverURLString`,`pageCount`,`currentPage`,`progressFraction`,`status`,`genre`,`rating`,`isFavorite`,`lastProgressDeltaPercent`,`lastSessionMinutes`,`lastSessionNotes`,`lastProgressUpdate`,`finishedDate`,`addedDate`,`openLibraryWorkKey`,`isbn`,`remoteId`,`notesJson`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BookEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getAuthor());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        if (entity.getCoverURLString() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCoverURLString());
        }
        if (entity.getPageCount() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getPageCount());
        }
        if (entity.getCurrentPage() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getCurrentPage());
        }
        if (entity.getProgressFraction() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getProgressFraction());
        }
        statement.bindString(9, entity.getStatus());
        if (entity.getGenre() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getGenre());
        }
        if (entity.getRating() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getRating());
        }
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(12, _tmp);
        if (entity.getLastProgressDeltaPercent() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getLastProgressDeltaPercent());
        }
        if (entity.getLastSessionMinutes() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getLastSessionMinutes());
        }
        if (entity.getLastSessionNotes() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getLastSessionNotes());
        }
        if (entity.getLastProgressUpdate() == null) {
          statement.bindNull(16);
        } else {
          statement.bindLong(16, entity.getLastProgressUpdate());
        }
        if (entity.getFinishedDate() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getFinishedDate());
        }
        if (entity.getAddedDate() == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, entity.getAddedDate());
        }
        if (entity.getOpenLibraryWorkKey() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getOpenLibraryWorkKey());
        }
        if (entity.getIsbn() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getIsbn());
        }
        if (entity.getRemoteId() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getRemoteId());
        }
        if (entity.getNotesJson() == null) {
          statement.bindNull(22);
        } else {
          statement.bindString(22, entity.getNotesJson());
        }
      }
    };
    this.__deletionAdapterOfBookEntity = new EntityDeletionOrUpdateAdapter<BookEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `books` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BookEntity entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfBookEntity = new EntityDeletionOrUpdateAdapter<BookEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `books` SET `id` = ?,`title` = ?,`author` = ?,`description` = ?,`coverURLString` = ?,`pageCount` = ?,`currentPage` = ?,`progressFraction` = ?,`status` = ?,`genre` = ?,`rating` = ?,`isFavorite` = ?,`lastProgressDeltaPercent` = ?,`lastSessionMinutes` = ?,`lastSessionNotes` = ?,`lastProgressUpdate` = ?,`finishedDate` = ?,`addedDate` = ?,`openLibraryWorkKey` = ?,`isbn` = ?,`remoteId` = ?,`notesJson` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BookEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getAuthor());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        if (entity.getCoverURLString() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCoverURLString());
        }
        if (entity.getPageCount() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getPageCount());
        }
        if (entity.getCurrentPage() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getCurrentPage());
        }
        if (entity.getProgressFraction() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getProgressFraction());
        }
        statement.bindString(9, entity.getStatus());
        if (entity.getGenre() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getGenre());
        }
        if (entity.getRating() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getRating());
        }
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(12, _tmp);
        if (entity.getLastProgressDeltaPercent() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getLastProgressDeltaPercent());
        }
        if (entity.getLastSessionMinutes() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getLastSessionMinutes());
        }
        if (entity.getLastSessionNotes() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getLastSessionNotes());
        }
        if (entity.getLastProgressUpdate() == null) {
          statement.bindNull(16);
        } else {
          statement.bindLong(16, entity.getLastProgressUpdate());
        }
        if (entity.getFinishedDate() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getFinishedDate());
        }
        if (entity.getAddedDate() == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, entity.getAddedDate());
        }
        if (entity.getOpenLibraryWorkKey() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getOpenLibraryWorkKey());
        }
        if (entity.getIsbn() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getIsbn());
        }
        if (entity.getRemoteId() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getRemoteId());
        }
        if (entity.getNotesJson() == null) {
          statement.bindNull(22);
        } else {
          statement.bindString(22, entity.getNotesJson());
        }
        statement.bindString(23, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteBookById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM books WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertBook(final BookEntity book, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBookEntity.insert(book);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertBooks(final List<BookEntity> books,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBookEntity.insert(books);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBook(final BookEntity book, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBookEntity.handle(book);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBook(final BookEntity book, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBookEntity.handle(book);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBookById(final String bookId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteBookById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, bookId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteBookById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<BookEntity>> getAllBooks() {
    final String _sql = "SELECT * FROM books ORDER BY lastProgressUpdate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<List<BookEntity>>() {
      @Override
      @NonNull
      public List<BookEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCoverURLString = CursorUtil.getColumnIndexOrThrow(_cursor, "coverURLString");
          final int _cursorIndexOfPageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pageCount");
          final int _cursorIndexOfCurrentPage = CursorUtil.getColumnIndexOrThrow(_cursor, "currentPage");
          final int _cursorIndexOfProgressFraction = CursorUtil.getColumnIndexOrThrow(_cursor, "progressFraction");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfLastProgressDeltaPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "lastProgressDeltaPercent");
          final int _cursorIndexOfLastSessionMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSessionMinutes");
          final int _cursorIndexOfLastSessionNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSessionNotes");
          final int _cursorIndexOfLastProgressUpdate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastProgressUpdate");
          final int _cursorIndexOfFinishedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "finishedDate");
          final int _cursorIndexOfAddedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "addedDate");
          final int _cursorIndexOfOpenLibraryWorkKey = CursorUtil.getColumnIndexOrThrow(_cursor, "openLibraryWorkKey");
          final int _cursorIndexOfIsbn = CursorUtil.getColumnIndexOrThrow(_cursor, "isbn");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfNotesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "notesJson");
          final List<BookEntity> _result = new ArrayList<BookEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BookEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCoverURLString;
            if (_cursor.isNull(_cursorIndexOfCoverURLString)) {
              _tmpCoverURLString = null;
            } else {
              _tmpCoverURLString = _cursor.getString(_cursorIndexOfCoverURLString);
            }
            final Integer _tmpPageCount;
            if (_cursor.isNull(_cursorIndexOfPageCount)) {
              _tmpPageCount = null;
            } else {
              _tmpPageCount = _cursor.getInt(_cursorIndexOfPageCount);
            }
            final Integer _tmpCurrentPage;
            if (_cursor.isNull(_cursorIndexOfCurrentPage)) {
              _tmpCurrentPage = null;
            } else {
              _tmpCurrentPage = _cursor.getInt(_cursorIndexOfCurrentPage);
            }
            final Double _tmpProgressFraction;
            if (_cursor.isNull(_cursorIndexOfProgressFraction)) {
              _tmpProgressFraction = null;
            } else {
              _tmpProgressFraction = _cursor.getDouble(_cursorIndexOfProgressFraction);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpGenre;
            if (_cursor.isNull(_cursorIndexOfGenre)) {
              _tmpGenre = null;
            } else {
              _tmpGenre = _cursor.getString(_cursorIndexOfGenre);
            }
            final Double _tmpRating;
            if (_cursor.isNull(_cursorIndexOfRating)) {
              _tmpRating = null;
            } else {
              _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Integer _tmpLastProgressDeltaPercent;
            if (_cursor.isNull(_cursorIndexOfLastProgressDeltaPercent)) {
              _tmpLastProgressDeltaPercent = null;
            } else {
              _tmpLastProgressDeltaPercent = _cursor.getInt(_cursorIndexOfLastProgressDeltaPercent);
            }
            final Integer _tmpLastSessionMinutes;
            if (_cursor.isNull(_cursorIndexOfLastSessionMinutes)) {
              _tmpLastSessionMinutes = null;
            } else {
              _tmpLastSessionMinutes = _cursor.getInt(_cursorIndexOfLastSessionMinutes);
            }
            final String _tmpLastSessionNotes;
            if (_cursor.isNull(_cursorIndexOfLastSessionNotes)) {
              _tmpLastSessionNotes = null;
            } else {
              _tmpLastSessionNotes = _cursor.getString(_cursorIndexOfLastSessionNotes);
            }
            final Long _tmpLastProgressUpdate;
            if (_cursor.isNull(_cursorIndexOfLastProgressUpdate)) {
              _tmpLastProgressUpdate = null;
            } else {
              _tmpLastProgressUpdate = _cursor.getLong(_cursorIndexOfLastProgressUpdate);
            }
            final Long _tmpFinishedDate;
            if (_cursor.isNull(_cursorIndexOfFinishedDate)) {
              _tmpFinishedDate = null;
            } else {
              _tmpFinishedDate = _cursor.getLong(_cursorIndexOfFinishedDate);
            }
            final Long _tmpAddedDate;
            if (_cursor.isNull(_cursorIndexOfAddedDate)) {
              _tmpAddedDate = null;
            } else {
              _tmpAddedDate = _cursor.getLong(_cursorIndexOfAddedDate);
            }
            final String _tmpOpenLibraryWorkKey;
            if (_cursor.isNull(_cursorIndexOfOpenLibraryWorkKey)) {
              _tmpOpenLibraryWorkKey = null;
            } else {
              _tmpOpenLibraryWorkKey = _cursor.getString(_cursorIndexOfOpenLibraryWorkKey);
            }
            final String _tmpIsbn;
            if (_cursor.isNull(_cursorIndexOfIsbn)) {
              _tmpIsbn = null;
            } else {
              _tmpIsbn = _cursor.getString(_cursorIndexOfIsbn);
            }
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpNotesJson;
            if (_cursor.isNull(_cursorIndexOfNotesJson)) {
              _tmpNotesJson = null;
            } else {
              _tmpNotesJson = _cursor.getString(_cursorIndexOfNotesJson);
            }
            _item = new BookEntity(_tmpId,_tmpTitle,_tmpAuthor,_tmpDescription,_tmpCoverURLString,_tmpPageCount,_tmpCurrentPage,_tmpProgressFraction,_tmpStatus,_tmpGenre,_tmpRating,_tmpIsFavorite,_tmpLastProgressDeltaPercent,_tmpLastSessionMinutes,_tmpLastSessionNotes,_tmpLastProgressUpdate,_tmpFinishedDate,_tmpAddedDate,_tmpOpenLibraryWorkKey,_tmpIsbn,_tmpRemoteId,_tmpNotesJson);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<BookEntity>> getCurrentlyReadingBooks() {
    final String _sql = "SELECT * FROM books WHERE status = 'READING' ORDER BY lastProgressUpdate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<List<BookEntity>>() {
      @Override
      @NonNull
      public List<BookEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCoverURLString = CursorUtil.getColumnIndexOrThrow(_cursor, "coverURLString");
          final int _cursorIndexOfPageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pageCount");
          final int _cursorIndexOfCurrentPage = CursorUtil.getColumnIndexOrThrow(_cursor, "currentPage");
          final int _cursorIndexOfProgressFraction = CursorUtil.getColumnIndexOrThrow(_cursor, "progressFraction");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfLastProgressDeltaPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "lastProgressDeltaPercent");
          final int _cursorIndexOfLastSessionMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSessionMinutes");
          final int _cursorIndexOfLastSessionNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSessionNotes");
          final int _cursorIndexOfLastProgressUpdate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastProgressUpdate");
          final int _cursorIndexOfFinishedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "finishedDate");
          final int _cursorIndexOfAddedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "addedDate");
          final int _cursorIndexOfOpenLibraryWorkKey = CursorUtil.getColumnIndexOrThrow(_cursor, "openLibraryWorkKey");
          final int _cursorIndexOfIsbn = CursorUtil.getColumnIndexOrThrow(_cursor, "isbn");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfNotesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "notesJson");
          final List<BookEntity> _result = new ArrayList<BookEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BookEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCoverURLString;
            if (_cursor.isNull(_cursorIndexOfCoverURLString)) {
              _tmpCoverURLString = null;
            } else {
              _tmpCoverURLString = _cursor.getString(_cursorIndexOfCoverURLString);
            }
            final Integer _tmpPageCount;
            if (_cursor.isNull(_cursorIndexOfPageCount)) {
              _tmpPageCount = null;
            } else {
              _tmpPageCount = _cursor.getInt(_cursorIndexOfPageCount);
            }
            final Integer _tmpCurrentPage;
            if (_cursor.isNull(_cursorIndexOfCurrentPage)) {
              _tmpCurrentPage = null;
            } else {
              _tmpCurrentPage = _cursor.getInt(_cursorIndexOfCurrentPage);
            }
            final Double _tmpProgressFraction;
            if (_cursor.isNull(_cursorIndexOfProgressFraction)) {
              _tmpProgressFraction = null;
            } else {
              _tmpProgressFraction = _cursor.getDouble(_cursorIndexOfProgressFraction);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpGenre;
            if (_cursor.isNull(_cursorIndexOfGenre)) {
              _tmpGenre = null;
            } else {
              _tmpGenre = _cursor.getString(_cursorIndexOfGenre);
            }
            final Double _tmpRating;
            if (_cursor.isNull(_cursorIndexOfRating)) {
              _tmpRating = null;
            } else {
              _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Integer _tmpLastProgressDeltaPercent;
            if (_cursor.isNull(_cursorIndexOfLastProgressDeltaPercent)) {
              _tmpLastProgressDeltaPercent = null;
            } else {
              _tmpLastProgressDeltaPercent = _cursor.getInt(_cursorIndexOfLastProgressDeltaPercent);
            }
            final Integer _tmpLastSessionMinutes;
            if (_cursor.isNull(_cursorIndexOfLastSessionMinutes)) {
              _tmpLastSessionMinutes = null;
            } else {
              _tmpLastSessionMinutes = _cursor.getInt(_cursorIndexOfLastSessionMinutes);
            }
            final String _tmpLastSessionNotes;
            if (_cursor.isNull(_cursorIndexOfLastSessionNotes)) {
              _tmpLastSessionNotes = null;
            } else {
              _tmpLastSessionNotes = _cursor.getString(_cursorIndexOfLastSessionNotes);
            }
            final Long _tmpLastProgressUpdate;
            if (_cursor.isNull(_cursorIndexOfLastProgressUpdate)) {
              _tmpLastProgressUpdate = null;
            } else {
              _tmpLastProgressUpdate = _cursor.getLong(_cursorIndexOfLastProgressUpdate);
            }
            final Long _tmpFinishedDate;
            if (_cursor.isNull(_cursorIndexOfFinishedDate)) {
              _tmpFinishedDate = null;
            } else {
              _tmpFinishedDate = _cursor.getLong(_cursorIndexOfFinishedDate);
            }
            final Long _tmpAddedDate;
            if (_cursor.isNull(_cursorIndexOfAddedDate)) {
              _tmpAddedDate = null;
            } else {
              _tmpAddedDate = _cursor.getLong(_cursorIndexOfAddedDate);
            }
            final String _tmpOpenLibraryWorkKey;
            if (_cursor.isNull(_cursorIndexOfOpenLibraryWorkKey)) {
              _tmpOpenLibraryWorkKey = null;
            } else {
              _tmpOpenLibraryWorkKey = _cursor.getString(_cursorIndexOfOpenLibraryWorkKey);
            }
            final String _tmpIsbn;
            if (_cursor.isNull(_cursorIndexOfIsbn)) {
              _tmpIsbn = null;
            } else {
              _tmpIsbn = _cursor.getString(_cursorIndexOfIsbn);
            }
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpNotesJson;
            if (_cursor.isNull(_cursorIndexOfNotesJson)) {
              _tmpNotesJson = null;
            } else {
              _tmpNotesJson = _cursor.getString(_cursorIndexOfNotesJson);
            }
            _item = new BookEntity(_tmpId,_tmpTitle,_tmpAuthor,_tmpDescription,_tmpCoverURLString,_tmpPageCount,_tmpCurrentPage,_tmpProgressFraction,_tmpStatus,_tmpGenre,_tmpRating,_tmpIsFavorite,_tmpLastProgressDeltaPercent,_tmpLastSessionMinutes,_tmpLastSessionNotes,_tmpLastProgressUpdate,_tmpFinishedDate,_tmpAddedDate,_tmpOpenLibraryWorkKey,_tmpIsbn,_tmpRemoteId,_tmpNotesJson);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<BookEntity>> getFinishedBooks() {
    final String _sql = "SELECT * FROM books WHERE status = 'FINISHED' ORDER BY finishedDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<List<BookEntity>>() {
      @Override
      @NonNull
      public List<BookEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCoverURLString = CursorUtil.getColumnIndexOrThrow(_cursor, "coverURLString");
          final int _cursorIndexOfPageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pageCount");
          final int _cursorIndexOfCurrentPage = CursorUtil.getColumnIndexOrThrow(_cursor, "currentPage");
          final int _cursorIndexOfProgressFraction = CursorUtil.getColumnIndexOrThrow(_cursor, "progressFraction");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfLastProgressDeltaPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "lastProgressDeltaPercent");
          final int _cursorIndexOfLastSessionMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSessionMinutes");
          final int _cursorIndexOfLastSessionNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSessionNotes");
          final int _cursorIndexOfLastProgressUpdate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastProgressUpdate");
          final int _cursorIndexOfFinishedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "finishedDate");
          final int _cursorIndexOfAddedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "addedDate");
          final int _cursorIndexOfOpenLibraryWorkKey = CursorUtil.getColumnIndexOrThrow(_cursor, "openLibraryWorkKey");
          final int _cursorIndexOfIsbn = CursorUtil.getColumnIndexOrThrow(_cursor, "isbn");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfNotesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "notesJson");
          final List<BookEntity> _result = new ArrayList<BookEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BookEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCoverURLString;
            if (_cursor.isNull(_cursorIndexOfCoverURLString)) {
              _tmpCoverURLString = null;
            } else {
              _tmpCoverURLString = _cursor.getString(_cursorIndexOfCoverURLString);
            }
            final Integer _tmpPageCount;
            if (_cursor.isNull(_cursorIndexOfPageCount)) {
              _tmpPageCount = null;
            } else {
              _tmpPageCount = _cursor.getInt(_cursorIndexOfPageCount);
            }
            final Integer _tmpCurrentPage;
            if (_cursor.isNull(_cursorIndexOfCurrentPage)) {
              _tmpCurrentPage = null;
            } else {
              _tmpCurrentPage = _cursor.getInt(_cursorIndexOfCurrentPage);
            }
            final Double _tmpProgressFraction;
            if (_cursor.isNull(_cursorIndexOfProgressFraction)) {
              _tmpProgressFraction = null;
            } else {
              _tmpProgressFraction = _cursor.getDouble(_cursorIndexOfProgressFraction);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpGenre;
            if (_cursor.isNull(_cursorIndexOfGenre)) {
              _tmpGenre = null;
            } else {
              _tmpGenre = _cursor.getString(_cursorIndexOfGenre);
            }
            final Double _tmpRating;
            if (_cursor.isNull(_cursorIndexOfRating)) {
              _tmpRating = null;
            } else {
              _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Integer _tmpLastProgressDeltaPercent;
            if (_cursor.isNull(_cursorIndexOfLastProgressDeltaPercent)) {
              _tmpLastProgressDeltaPercent = null;
            } else {
              _tmpLastProgressDeltaPercent = _cursor.getInt(_cursorIndexOfLastProgressDeltaPercent);
            }
            final Integer _tmpLastSessionMinutes;
            if (_cursor.isNull(_cursorIndexOfLastSessionMinutes)) {
              _tmpLastSessionMinutes = null;
            } else {
              _tmpLastSessionMinutes = _cursor.getInt(_cursorIndexOfLastSessionMinutes);
            }
            final String _tmpLastSessionNotes;
            if (_cursor.isNull(_cursorIndexOfLastSessionNotes)) {
              _tmpLastSessionNotes = null;
            } else {
              _tmpLastSessionNotes = _cursor.getString(_cursorIndexOfLastSessionNotes);
            }
            final Long _tmpLastProgressUpdate;
            if (_cursor.isNull(_cursorIndexOfLastProgressUpdate)) {
              _tmpLastProgressUpdate = null;
            } else {
              _tmpLastProgressUpdate = _cursor.getLong(_cursorIndexOfLastProgressUpdate);
            }
            final Long _tmpFinishedDate;
            if (_cursor.isNull(_cursorIndexOfFinishedDate)) {
              _tmpFinishedDate = null;
            } else {
              _tmpFinishedDate = _cursor.getLong(_cursorIndexOfFinishedDate);
            }
            final Long _tmpAddedDate;
            if (_cursor.isNull(_cursorIndexOfAddedDate)) {
              _tmpAddedDate = null;
            } else {
              _tmpAddedDate = _cursor.getLong(_cursorIndexOfAddedDate);
            }
            final String _tmpOpenLibraryWorkKey;
            if (_cursor.isNull(_cursorIndexOfOpenLibraryWorkKey)) {
              _tmpOpenLibraryWorkKey = null;
            } else {
              _tmpOpenLibraryWorkKey = _cursor.getString(_cursorIndexOfOpenLibraryWorkKey);
            }
            final String _tmpIsbn;
            if (_cursor.isNull(_cursorIndexOfIsbn)) {
              _tmpIsbn = null;
            } else {
              _tmpIsbn = _cursor.getString(_cursorIndexOfIsbn);
            }
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpNotesJson;
            if (_cursor.isNull(_cursorIndexOfNotesJson)) {
              _tmpNotesJson = null;
            } else {
              _tmpNotesJson = _cursor.getString(_cursorIndexOfNotesJson);
            }
            _item = new BookEntity(_tmpId,_tmpTitle,_tmpAuthor,_tmpDescription,_tmpCoverURLString,_tmpPageCount,_tmpCurrentPage,_tmpProgressFraction,_tmpStatus,_tmpGenre,_tmpRating,_tmpIsFavorite,_tmpLastProgressDeltaPercent,_tmpLastSessionMinutes,_tmpLastSessionNotes,_tmpLastProgressUpdate,_tmpFinishedDate,_tmpAddedDate,_tmpOpenLibraryWorkKey,_tmpIsbn,_tmpRemoteId,_tmpNotesJson);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getBookById(final String bookId,
      final Continuation<? super BookEntity> $completion) {
    final String _sql = "SELECT * FROM books WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, bookId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BookEntity>() {
      @Override
      @Nullable
      public BookEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCoverURLString = CursorUtil.getColumnIndexOrThrow(_cursor, "coverURLString");
          final int _cursorIndexOfPageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pageCount");
          final int _cursorIndexOfCurrentPage = CursorUtil.getColumnIndexOrThrow(_cursor, "currentPage");
          final int _cursorIndexOfProgressFraction = CursorUtil.getColumnIndexOrThrow(_cursor, "progressFraction");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfLastProgressDeltaPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "lastProgressDeltaPercent");
          final int _cursorIndexOfLastSessionMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSessionMinutes");
          final int _cursorIndexOfLastSessionNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSessionNotes");
          final int _cursorIndexOfLastProgressUpdate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastProgressUpdate");
          final int _cursorIndexOfFinishedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "finishedDate");
          final int _cursorIndexOfAddedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "addedDate");
          final int _cursorIndexOfOpenLibraryWorkKey = CursorUtil.getColumnIndexOrThrow(_cursor, "openLibraryWorkKey");
          final int _cursorIndexOfIsbn = CursorUtil.getColumnIndexOrThrow(_cursor, "isbn");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfNotesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "notesJson");
          final BookEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCoverURLString;
            if (_cursor.isNull(_cursorIndexOfCoverURLString)) {
              _tmpCoverURLString = null;
            } else {
              _tmpCoverURLString = _cursor.getString(_cursorIndexOfCoverURLString);
            }
            final Integer _tmpPageCount;
            if (_cursor.isNull(_cursorIndexOfPageCount)) {
              _tmpPageCount = null;
            } else {
              _tmpPageCount = _cursor.getInt(_cursorIndexOfPageCount);
            }
            final Integer _tmpCurrentPage;
            if (_cursor.isNull(_cursorIndexOfCurrentPage)) {
              _tmpCurrentPage = null;
            } else {
              _tmpCurrentPage = _cursor.getInt(_cursorIndexOfCurrentPage);
            }
            final Double _tmpProgressFraction;
            if (_cursor.isNull(_cursorIndexOfProgressFraction)) {
              _tmpProgressFraction = null;
            } else {
              _tmpProgressFraction = _cursor.getDouble(_cursorIndexOfProgressFraction);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpGenre;
            if (_cursor.isNull(_cursorIndexOfGenre)) {
              _tmpGenre = null;
            } else {
              _tmpGenre = _cursor.getString(_cursorIndexOfGenre);
            }
            final Double _tmpRating;
            if (_cursor.isNull(_cursorIndexOfRating)) {
              _tmpRating = null;
            } else {
              _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Integer _tmpLastProgressDeltaPercent;
            if (_cursor.isNull(_cursorIndexOfLastProgressDeltaPercent)) {
              _tmpLastProgressDeltaPercent = null;
            } else {
              _tmpLastProgressDeltaPercent = _cursor.getInt(_cursorIndexOfLastProgressDeltaPercent);
            }
            final Integer _tmpLastSessionMinutes;
            if (_cursor.isNull(_cursorIndexOfLastSessionMinutes)) {
              _tmpLastSessionMinutes = null;
            } else {
              _tmpLastSessionMinutes = _cursor.getInt(_cursorIndexOfLastSessionMinutes);
            }
            final String _tmpLastSessionNotes;
            if (_cursor.isNull(_cursorIndexOfLastSessionNotes)) {
              _tmpLastSessionNotes = null;
            } else {
              _tmpLastSessionNotes = _cursor.getString(_cursorIndexOfLastSessionNotes);
            }
            final Long _tmpLastProgressUpdate;
            if (_cursor.isNull(_cursorIndexOfLastProgressUpdate)) {
              _tmpLastProgressUpdate = null;
            } else {
              _tmpLastProgressUpdate = _cursor.getLong(_cursorIndexOfLastProgressUpdate);
            }
            final Long _tmpFinishedDate;
            if (_cursor.isNull(_cursorIndexOfFinishedDate)) {
              _tmpFinishedDate = null;
            } else {
              _tmpFinishedDate = _cursor.getLong(_cursorIndexOfFinishedDate);
            }
            final Long _tmpAddedDate;
            if (_cursor.isNull(_cursorIndexOfAddedDate)) {
              _tmpAddedDate = null;
            } else {
              _tmpAddedDate = _cursor.getLong(_cursorIndexOfAddedDate);
            }
            final String _tmpOpenLibraryWorkKey;
            if (_cursor.isNull(_cursorIndexOfOpenLibraryWorkKey)) {
              _tmpOpenLibraryWorkKey = null;
            } else {
              _tmpOpenLibraryWorkKey = _cursor.getString(_cursorIndexOfOpenLibraryWorkKey);
            }
            final String _tmpIsbn;
            if (_cursor.isNull(_cursorIndexOfIsbn)) {
              _tmpIsbn = null;
            } else {
              _tmpIsbn = _cursor.getString(_cursorIndexOfIsbn);
            }
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpNotesJson;
            if (_cursor.isNull(_cursorIndexOfNotesJson)) {
              _tmpNotesJson = null;
            } else {
              _tmpNotesJson = _cursor.getString(_cursorIndexOfNotesJson);
            }
            _result = new BookEntity(_tmpId,_tmpTitle,_tmpAuthor,_tmpDescription,_tmpCoverURLString,_tmpPageCount,_tmpCurrentPage,_tmpProgressFraction,_tmpStatus,_tmpGenre,_tmpRating,_tmpIsFavorite,_tmpLastProgressDeltaPercent,_tmpLastSessionMinutes,_tmpLastSessionNotes,_tmpLastProgressUpdate,_tmpFinishedDate,_tmpAddedDate,_tmpOpenLibraryWorkKey,_tmpIsbn,_tmpRemoteId,_tmpNotesJson);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<BookEntity>> searchBooks(final String query) {
    final String _sql = "SELECT * FROM books WHERE title LIKE '%' || ? || '%' OR author LIKE '%' || ? || '%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<List<BookEntity>>() {
      @Override
      @NonNull
      public List<BookEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCoverURLString = CursorUtil.getColumnIndexOrThrow(_cursor, "coverURLString");
          final int _cursorIndexOfPageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pageCount");
          final int _cursorIndexOfCurrentPage = CursorUtil.getColumnIndexOrThrow(_cursor, "currentPage");
          final int _cursorIndexOfProgressFraction = CursorUtil.getColumnIndexOrThrow(_cursor, "progressFraction");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfLastProgressDeltaPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "lastProgressDeltaPercent");
          final int _cursorIndexOfLastSessionMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSessionMinutes");
          final int _cursorIndexOfLastSessionNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSessionNotes");
          final int _cursorIndexOfLastProgressUpdate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastProgressUpdate");
          final int _cursorIndexOfFinishedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "finishedDate");
          final int _cursorIndexOfAddedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "addedDate");
          final int _cursorIndexOfOpenLibraryWorkKey = CursorUtil.getColumnIndexOrThrow(_cursor, "openLibraryWorkKey");
          final int _cursorIndexOfIsbn = CursorUtil.getColumnIndexOrThrow(_cursor, "isbn");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteId");
          final int _cursorIndexOfNotesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "notesJson");
          final List<BookEntity> _result = new ArrayList<BookEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BookEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCoverURLString;
            if (_cursor.isNull(_cursorIndexOfCoverURLString)) {
              _tmpCoverURLString = null;
            } else {
              _tmpCoverURLString = _cursor.getString(_cursorIndexOfCoverURLString);
            }
            final Integer _tmpPageCount;
            if (_cursor.isNull(_cursorIndexOfPageCount)) {
              _tmpPageCount = null;
            } else {
              _tmpPageCount = _cursor.getInt(_cursorIndexOfPageCount);
            }
            final Integer _tmpCurrentPage;
            if (_cursor.isNull(_cursorIndexOfCurrentPage)) {
              _tmpCurrentPage = null;
            } else {
              _tmpCurrentPage = _cursor.getInt(_cursorIndexOfCurrentPage);
            }
            final Double _tmpProgressFraction;
            if (_cursor.isNull(_cursorIndexOfProgressFraction)) {
              _tmpProgressFraction = null;
            } else {
              _tmpProgressFraction = _cursor.getDouble(_cursorIndexOfProgressFraction);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpGenre;
            if (_cursor.isNull(_cursorIndexOfGenre)) {
              _tmpGenre = null;
            } else {
              _tmpGenre = _cursor.getString(_cursorIndexOfGenre);
            }
            final Double _tmpRating;
            if (_cursor.isNull(_cursorIndexOfRating)) {
              _tmpRating = null;
            } else {
              _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
            }
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final Integer _tmpLastProgressDeltaPercent;
            if (_cursor.isNull(_cursorIndexOfLastProgressDeltaPercent)) {
              _tmpLastProgressDeltaPercent = null;
            } else {
              _tmpLastProgressDeltaPercent = _cursor.getInt(_cursorIndexOfLastProgressDeltaPercent);
            }
            final Integer _tmpLastSessionMinutes;
            if (_cursor.isNull(_cursorIndexOfLastSessionMinutes)) {
              _tmpLastSessionMinutes = null;
            } else {
              _tmpLastSessionMinutes = _cursor.getInt(_cursorIndexOfLastSessionMinutes);
            }
            final String _tmpLastSessionNotes;
            if (_cursor.isNull(_cursorIndexOfLastSessionNotes)) {
              _tmpLastSessionNotes = null;
            } else {
              _tmpLastSessionNotes = _cursor.getString(_cursorIndexOfLastSessionNotes);
            }
            final Long _tmpLastProgressUpdate;
            if (_cursor.isNull(_cursorIndexOfLastProgressUpdate)) {
              _tmpLastProgressUpdate = null;
            } else {
              _tmpLastProgressUpdate = _cursor.getLong(_cursorIndexOfLastProgressUpdate);
            }
            final Long _tmpFinishedDate;
            if (_cursor.isNull(_cursorIndexOfFinishedDate)) {
              _tmpFinishedDate = null;
            } else {
              _tmpFinishedDate = _cursor.getLong(_cursorIndexOfFinishedDate);
            }
            final Long _tmpAddedDate;
            if (_cursor.isNull(_cursorIndexOfAddedDate)) {
              _tmpAddedDate = null;
            } else {
              _tmpAddedDate = _cursor.getLong(_cursorIndexOfAddedDate);
            }
            final String _tmpOpenLibraryWorkKey;
            if (_cursor.isNull(_cursorIndexOfOpenLibraryWorkKey)) {
              _tmpOpenLibraryWorkKey = null;
            } else {
              _tmpOpenLibraryWorkKey = _cursor.getString(_cursorIndexOfOpenLibraryWorkKey);
            }
            final String _tmpIsbn;
            if (_cursor.isNull(_cursorIndexOfIsbn)) {
              _tmpIsbn = null;
            } else {
              _tmpIsbn = _cursor.getString(_cursorIndexOfIsbn);
            }
            final String _tmpRemoteId;
            if (_cursor.isNull(_cursorIndexOfRemoteId)) {
              _tmpRemoteId = null;
            } else {
              _tmpRemoteId = _cursor.getString(_cursorIndexOfRemoteId);
            }
            final String _tmpNotesJson;
            if (_cursor.isNull(_cursorIndexOfNotesJson)) {
              _tmpNotesJson = null;
            } else {
              _tmpNotesJson = _cursor.getString(_cursorIndexOfNotesJson);
            }
            _item = new BookEntity(_tmpId,_tmpTitle,_tmpAuthor,_tmpDescription,_tmpCoverURLString,_tmpPageCount,_tmpCurrentPage,_tmpProgressFraction,_tmpStatus,_tmpGenre,_tmpRating,_tmpIsFavorite,_tmpLastProgressDeltaPercent,_tmpLastSessionMinutes,_tmpLastSessionNotes,_tmpLastProgressUpdate,_tmpFinishedDate,_tmpAddedDate,_tmpOpenLibraryWorkKey,_tmpIsbn,_tmpRemoteId,_tmpNotesJson);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> getFinishedBooksCount() {
    final String _sql = "SELECT COUNT(*) FROM books WHERE status = 'FINISHED'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> getTotalPagesRead() {
    final String _sql = "SELECT SUM(currentPage) FROM books";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
