# CECC Care Companion - Testing Guide

## Overview

This document provides comprehensive testing procedures for the CECC Care Companion Android application before deployment.

## Test Environment Setup

### Required Devices
- **Primary Test Device**: Android 10+ smartphone
- **Secondary Test Device**: Android 7.0+ tablet
- **Tertiary Test Device**: Android 12+ device for latest features

### Required Software
- Android Studio 2023.3+
- Android SDK 24-35
- Google Drive account for sync testing

## Testing Phases

### Phase 1: Build & Installation Testing

#### 1.1 Build Verification
```bash
# In Android Studio:
1. Open project: File → Open → CECCCareCompanion/
2. Sync Gradle: Wait for "Gradle sync finished"
3. Build APK: Build → Build Bundle(s)/APK(s) → Build APK(s)
4. Verify no build errors in "Build" tab
```

**Expected Result**: ✅ APK builds successfully without errors

#### 1.2 Installation Testing
1. Deploy to test device via USB debugging
2. Verify app installs without errors
3. Check app appears in launcher
4. Verify app opens without crashing

**Expected Result**: ✅ App installs and launches successfully

### Phase 2: Core Functionality Testing

#### 2.1 Navigation Testing
- [ ] Home screen loads correctly
- [ ] All navigation buttons work
- [ ] Back button functions properly
- [ ] No navigation loops or dead ends

#### 2.2 Database Testing
- [ ] App creates local database on first run
- [ ] Data persists between app sessions
- [ ] No database corruption errors

#### 2.3 Form Testing
**Baseline Form:**
- [ ] All input fields accept data
- [ ] QALY calculation displays correctly
- [ ] Form saves without errors
- [ ] Data appears in database

**Midline Form:**
- [ ] Loads baseline data correctly
- [ ] QALY comparison works
- [ ] Form saves successfully

**Endline Form:**
- [ ] Loads baseline and midline data
- [ ] Complete QALY trajectory displays
- [ ] Program evaluation saves correctly

#### 2.4 Screener Testing
**For each of 8 screeners:**
- [ ] Screener loads from JSON correctly
- [ ] All questions display properly
- [ ] Scoring calculates accurately
- [ ] Risk levels display correctly
- [ ] Data saves to database

### Phase 3: Data Quality Testing

#### 3.1 Input Validation Testing
**Test invalid inputs:**
- [ ] Negative age shows error
- [ ] Invalid participant ID shows error
- [ ] Invalid blood pressure shows error
- [ ] Empty required fields show error

#### 3.2 Data Persistence Testing
- [ ] Restart app and verify data remains
- [ ] Force close app and verify data integrity
- [ ] Test with low storage space

#### 3.3 QALY Calculation Testing
**Test QALY algorithm:**
- [ ] Age 60, good health = QALY ~0.9
- [ ] Age 80, poor health = QALY ~0.4
- [ ] Mobility aid reduces QALY appropriately
- [ ] Chronic conditions reduce QALY appropriately

### Phase 4: Advanced Features Testing

#### 4.1 Multi-language Testing
- [ ] Switch to Hindi - all text translates
- [ ] Switch to Kannada - all text translates
- [ ] Language persists between sessions
- [ ] Mixed language content works

#### 4.2 Sync Testing
**Google Drive Sync:**
- [ ] Select Drive folder successfully
- [ ] Sync button triggers upload
- [ ] Data appears in Drive as JSON
- [ ] Sync status updates correctly
- [ ] Offline sync queues properly

#### 4.3 Security Testing
**PIN Authentication:**
- [ ] Set 4-digit PIN successfully
- [ ] PIN validation works correctly
- [ ] Incorrect PIN shows error
- [ ] PIN persists between sessions

**Biometric Authentication:**
- [ ] Biometric toggle works (if device supports)
- [ ] Fingerprint authentication functions
- [ ] Fallback to PIN works

### Phase 5: Performance Testing

#### 5.1 Memory Testing
- [ ] Complete full baseline form without crashes
- [ ] Complete all 8 screeners without memory issues
- [ ] App remains responsive during heavy use

#### 5.2 Battery Testing
- [ ] Monitor battery usage during normal operation
- [ ] Background sync doesn't drain battery excessively

#### 5.3 Storage Testing
- [ ] App works with low storage space
- [ ] Large forms save successfully
- [ ] Database doesn't grow unbounded

### Phase 6: User Experience Testing

#### 6.1 Field Worker Simulation
**Test realistic usage scenarios:**
- [ ] Complete baseline → midline → endline workflow
- [ ] Enter data for 5+ participants
- [ ] Use all 8 screening tools
- [ ] Test emergency dialing feature

#### 6.2 Error Recovery Testing
- [ ] Test behavior with no internet
- [ ] Test with corrupted database
- [ ] Test with insufficient permissions
- [ ] Verify graceful error messages

## Testing Checklist

### Pre-deployment Requirements
- [ ] All 8 screeners function correctly
- [ ] All 3 forms (baseline/midline/endline) work
- [ ] QALY calculations are accurate
- [ ] Multi-language support complete
- [ ] Google Drive sync operational
- [ ] PIN/biometric authentication working
- [ ] Emergency features accessible
- [ ] Data export functional
- [ ] No critical bugs found
- [ ] Performance acceptable

### Success Metrics
- **Functionality**: 100% of features working
- **Reliability**: No crashes during normal use
- **Usability**: Field workers can complete assessments
- **Performance**: App responsive on target devices
- **Security**: Authentication and data protection working

## Bug Reporting Template

**Bug Report Format:**
```
Device: [Device model, Android version]
App Version: [v1.1.0]
Feature: [Specific feature being tested]
Steps to Reproduce:
1.
2.
3.
Expected Behavior:
Actual Behavior:
Severity: [Critical/High/Medium/Low]
Screenshot/Video: [Attach if available]
```

## Emergency Fixes

**If critical bugs found:**
1. Document bug with reproduction steps
2. Identify root cause
3. Implement fix
4. Test fix thoroughly
5. Re-test full application
6. Update version number if needed

## Deployment Readiness

**App is ready for deployment when:**
- ✅ All tests pass
- ✅ No critical or high-severity bugs
- ✅ Performance meets requirements
- ✅ All features functional
- ✅ Documentation complete
- ✅ User training materials ready

---

**Testing Status**: In Progress
**Last Updated**: [Current Date]
**Next Testing Phase**: [Next phase to complete]
