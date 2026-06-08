/**
 * Generic CSV export utility.
 *
 * Usage:
 *   exportToCsv('products.csv', products, ['id', 'name', 'price', 'stock']);
 *
 * If `columns` is omitted, every key of the first row is used.
 */
export function exportToCsv<T extends Record<string, unknown>>(
  filename: string,
  rows: T[],
  columns?: (keyof T)[],
): void {
  if (!rows.length) return;

  const cols = (columns ?? Object.keys(rows[0]!)) as string[];

  const escape = (value: unknown): string => {
    const str = value == null ? '' : String(value);
    // Wrap in quotes if it contains a comma, double-quote, or newline
    if (str.includes(',') || str.includes('"') || str.includes('\n')) {
      return `"${str.replace(/"/g, '""')}"`;
    }
    return str;
  };

  const header = cols.map(escape).join(',');
  const body = rows
    .map(row => cols.map(col => escape((row as Record<string, unknown>)[col])).join(','))
    .join('\n');

  const csv = `${header}\n${body}`;
  const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
  const url = URL.createObjectURL(blob);

  const link = document.createElement('a');
  link.href = url;
  link.download = filename.endsWith('.csv') ? filename : `${filename}.csv`;
  link.click();

  URL.revokeObjectURL(url);
}

/** Build a dated filename like "products_2025-06-07.csv" */
export function datestampedFilename(base: string): string {
  const date = new Date().toISOString().split('T')[0];
  return `${base}_${date}.csv`;
}
