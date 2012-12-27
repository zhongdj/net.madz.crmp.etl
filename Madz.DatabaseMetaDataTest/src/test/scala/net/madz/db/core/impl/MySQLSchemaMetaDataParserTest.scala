package net.madz.db.core.impl

import org.scalatest.FunSpec

class MySQLSchemaMetaDataParserTest extends FunSpec {

  describe("Parse an Empty Database") {
    it("should parse an empty database") {
      pending
    }
  }

  describe("Parse a single table Database") {
    it("should parse correct charsetEncoding with MySQL Parser") {
      pending
    }

    it("should parse correct storage engine with MySQL Parser") {
      pending
    }
  }

  describe("Parse columns from a single table Database") {
    it("should parse all kinds of data types defined in JDBC specification") {
      pending
    }

    it("should parse column with modifier NULLABLE") {
      pending
    }

    it("should parse column with modifier DEFAULT") {
      pending
    }

    it("should parse column with correct collation") {
      pending
    }
  }

  describe("Parse indexes from Database") {
    it("should parse single column PK index") {
      pending
    }

    it("should parse auto incremental index") {
      pending
    }

    it("should parse composite PK with multiple columns") {
      pending
    }
    
    it("should parse single column UNIQUE KEY") {
      pending
    }
    
    it("should parse composite UNIQUE KEY with multiple columns") {
      pending
    }
    
    it("should parse non-PK auto-incremental column") {
      pending
    }
    
    it("should parse single column index") {
      pending
    }
    
    it("should parse multiple column index with correct order") {
      pending
    }
    
    it("should parse single column FOREIGN KEY") {
      pending
    }
    
    it("should parse multiple columns FOREIGN KEY with correct order") {
      pending
    }
  }
}