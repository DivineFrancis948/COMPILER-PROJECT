export class CodeData {
    constructor() {
        this.sourceCode = '';
        this.language = '';
        this.testCases = {
          testCase1: '',
          testCase2: '',
          testCase3: '',
          testCase4: '',
          testCase5: '',
          testCase6: '',
          testCase7: '',
          testCase8: '',
          testCase9: '',
          testCase10: '',
        };
      }
    sourceCode: string;
    language: string;
    testCases: {
        testCase1: string;
        testCase2: string;
        testCase3: string;
        testCase4: string;
        testCase5: string;
        testCase6: string;
        testCase7: string;
        testCase8: string;
        testCase9: string;
        testCase10: string;
    };
  }