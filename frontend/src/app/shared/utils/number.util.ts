export function parseAndValidateId(id: any): number | null {
  if (id === null || id === undefined) {
    return null;
  }

  const numericId = Number(id);
  if (!Number.isInteger(numericId) || numericId <= 0) {
    return null;
  }

  return numericId;
}
