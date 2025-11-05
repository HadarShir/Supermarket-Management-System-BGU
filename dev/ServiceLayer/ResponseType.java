package ServiceLayer;

public enum ResponseType {
    Success,
    Created,
    Updated,
    Deleted,
    NotFound,
    InvalidInput,
    Unauthorized,
    InvalidRole,
    EmployeeNotAvailable,
    AlreadyAssigned,
    ShiftAlreadyPublished,
    CannotAssignHoliday,
    InvalidAction,
    SystemError
}